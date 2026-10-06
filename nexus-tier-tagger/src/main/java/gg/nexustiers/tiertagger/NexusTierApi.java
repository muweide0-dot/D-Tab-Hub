package gg.nexustiers.tiertagger;

import com.google.gson.Gson;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

final class NexusTierApi {
    private static final Gson GSON = new Gson();
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    private NexusTierApi() {
    }

    static CompletableFuture<NexusProfile> fetch(String ign) {
        String encodedIgn = URLEncoder.encode(ign, StandardCharsets.UTF_8);
        String endpoint = NexusTierTagger.getConfig().apiUrl
                + "/players/" + encodedIgn + "/tiers";
        HttpRequest request = HttpRequest.newBuilder(URI.create(endpoint))
                .header("Accept", "application/json")
                .GET()
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        throw new IllegalStateException("NexusTiers returned HTTP " + response.statusCode());
                    }
                    NexusProfile profile = GSON.fromJson(response.body(), NexusProfile.class);
                    if (profile == null) {
                        throw new IllegalStateException("NexusTiers returned an empty profile");
                    }
                    return profile;
                });
    }
}
