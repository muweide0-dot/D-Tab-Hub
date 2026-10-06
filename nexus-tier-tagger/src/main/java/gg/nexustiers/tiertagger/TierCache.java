package gg.nexustiers.tiertagger;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final class TierCache {
    private static final ConcurrentMap<String, CacheEntry> CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, CompletableFuture<NexusProfile>> IN_FLIGHT = new ConcurrentHashMap<>();

    private TierCache() {
    }

    static Optional<NexusProfile> get(String ign) {
        String key = normalize(ign);
        if (key.isEmpty()) return Optional.empty();

        long now = System.currentTimeMillis();
        CacheEntry cached = CACHE.get(key);
        if (cached != null && cached.expiresAt > now) {
            return Optional.ofNullable(cached.profile);
        }

        IN_FLIGHT.computeIfAbsent(key, ignored -> NexusTierApi.fetch(ign)
                .whenComplete((profile, error) -> {
                    long ttl = error == null
                            ? NexusTierTagger.getConfig().cacheMinutes * 60_000L
                            : 30_000L;
                    CACHE.put(key, new CacheEntry(profile, System.currentTimeMillis() + ttl));
                    IN_FLIGHT.remove(key);
                    if (error != null) {
                        NexusTierTagger.LOGGER.debug("Could not load NexusTiers profile for {}", ign, error);
                    }
                }));

        return Optional.empty();
    }

    static void clear() {
        CACHE.clear();
        IN_FLIGHT.clear();
    }

    private static String normalize(String ign) {
        return ign == null ? "" : ign.trim().toLowerCase(Locale.ROOT);
    }

    private record CacheEntry(NexusProfile profile, long expiresAt) {
    }
}
