package gg.nexustiers.tiertagger;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public final class NexusTierTagger implements ClientModInitializer {
    public static final String MOD_ID = "nexus-tier-tagger";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static NexusConfig config;

    @Override
    public void onInitializeClient() {
        config = NexusConfig.load();
        LOGGER.info("NexusTiers Tagger enabled; reading ranks from {}", config.apiUrl);
    }

    public static NexusConfig getConfig() {
        if (config == null) config = NexusConfig.load();
        return config;
    }

    public static boolean isEnabled() {
        return getConfig().enabled;
    }

    public static Component appendTier(String ign, Component original) {
        if (ign == null || ign.isBlank() || original == null) return original;

        return TierCache.get(ign)
                .flatMap(profile -> java.util.Optional.ofNullable(profile.tierFor(getConfig().kit)))
                .map(tier -> {
                    MutableComponent prefix = Component.literal("[" + tier.toUpperCase(Locale.ROOT) + "] ")
                            .withStyle(style -> style.withColor(colorFor(tier)));
                    return prefix.append(original);
                })
                .orElse(original);
    }

    public static void clearCache() {
        TierCache.clear();
    }

    private static int colorFor(String tier) {
        return switch (tier.toLowerCase(Locale.ROOT)) {
            case "ht1" -> 0xE8BA3A;
            case "lt1" -> 0xD5B355;
            case "ht2" -> 0xC4D3E7;
            case "lt2" -> 0xA0A7B2;
            case "ht3" -> 0xF89F5A;
            case "lt3" -> 0xC67B42;
            case "ht4" -> 0x81749A;
            case "lt4" -> 0x655B79;
            case "ht5" -> 0x8F82A8;
            case "lt5" -> 0x655B79;
            default -> ChatFormatting.GRAY.getColor() == null ? 0xD3D3D3 : ChatFormatting.GRAY.getColor();
        };
    }
}
