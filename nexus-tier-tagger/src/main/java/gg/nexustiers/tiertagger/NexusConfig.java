package gg.nexustiers.tiertagger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class NexusConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("nexus-tier-tagger.json");

    public boolean enabled = true;
    public boolean showInNametags = true;
    public boolean showInPlayerList = true;
    public String kit = "overall";
    public String apiUrl = "https://nexustierss-production.up.railway.app/api";
    public int cacheMinutes = 10;

    public static NexusConfig load() {
        if (!Files.exists(FILE)) {
            NexusConfig config = new NexusConfig();
            config.save();
            return config;
        }

        try {
            NexusConfig config = GSON.fromJson(Files.readString(FILE), NexusConfig.class);
            if (config == null) config = new NexusConfig();
            config.normalize();
            return config;
        } catch (Exception error) {
            NexusTierTagger.LOGGER.warn("Could not read {}, using defaults", FILE, error);
            return new NexusConfig();
        }
    }

    public void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(this));
        } catch (IOException error) {
            NexusTierTagger.LOGGER.warn("Could not save {}", FILE, error);
        }
    }

    private void normalize() {
        if (kit == null || kit.isBlank()) kit = "overall";
        kit = kit.trim().toLowerCase();
        if (apiUrl == null || apiUrl.isBlank()) apiUrl = "https://nexustierss-production.up.railway.app/api";
        apiUrl = apiUrl.trim().replaceAll("/+$", "");
        if (cacheMinutes < 1) cacheMinutes = 1;
        if (cacheMinutes > 1440) cacheMinutes = 1440;
    }
}
