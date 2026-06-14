package net.serverpackswitcher.config;

// Verified against: FabricLoader.java (Fabric Loader API)

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PackConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("serverpackswitcher");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("serverpackswitcher.json");

    private Map<String, PackEntry> packs = new HashMap<>();

    public record PackEntry(
        UUID id,
        String url,
        String hash,
        boolean required,
        String prompt
    ) {
        public PackEntry(UUID id, String url, String hash, boolean required, String prompt) {
            this.id = id != null ? id : UUID.nameUUIDFromBytes(url != null ? url.getBytes(StandardCharsets.UTF_8) : new byte[0]);
            this.url = url != null ? url : "";
            this.hash = hash != null ? hash : "";
            this.required = required;
            this.prompt = prompt;
        }
    }

    public Map<String, PackEntry> getPacks() {
        return packs;
    }

    public void load() {
        if (!Files.exists(CONFIG_PATH)) {
            LOGGER.info("Config file not found, creating default at {}", CONFIG_PATH);
            createDefaultConfig();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root != null && root.has("packs")) {
                Map<String, PackEntry> loadedPacks = GSON.fromJson(
                    root.get("packs"),
                    new TypeToken<Map<String, PackEntry>>() {}.getType()
                );
                if (loadedPacks != null) {
                    this.packs = new HashMap<>();
                    for (Map.Entry<String, PackEntry> entry : loadedPacks.entrySet()) {
                        String name = entry.getKey().toLowerCase();
                        PackEntry pack = entry.getValue();
                        PackEntry normalizedPack = new PackEntry(
                            pack.id(),
                            pack.url(),
                            pack.hash(),
                            pack.required(),
                            pack.prompt()
                        );
                        this.packs.put(name, normalizedPack);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load pack config", e);
        }
    }

    private void createDefaultConfig() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            packs = new HashMap<>();
            packs.put("plastic", new PackEntry(
                null,
                "https://example.com/plastic_pack.zip",
                "da39a3ee5e6b4b0d3255bfef95601890afd80709",
                false,
                "Would you like to apply the Plastic Resource Pack?"
            ));
            packs.put("default", new PackEntry(
                null,
                "https://example.com/default_server_pack.zip",
                "",
                false,
                null
            ));
            save();
        } catch (Exception e) {
            LOGGER.error("Failed to create default config", e);
        }
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
            Map<String, Object> root = new HashMap<>();
            root.put("packs", packs);
            GSON.toJson(root, writer);
        } catch (Exception e) {
            LOGGER.error("Failed to save pack config", e);
        }
    }
}
