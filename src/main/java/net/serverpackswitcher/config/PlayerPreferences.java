package net.serverpackswitcher.config;

// Verified against: FabricLoader.java (Fabric Loader API)

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public class PlayerPreferences {
    private static final Logger LOGGER = LoggerFactory.getLogger("serverpackswitcher");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PREFS_PATH = FabricLoader.getInstance().getConfigDir().resolve("serverpackswitcher_preferences.json");

    private Map<UUID, String> preferences = new HashMap<>();

    public void load() {
        if (!Files.exists(PREFS_PATH)) {
            preferences = new HashMap<>();
            return;
        }

        try (Reader reader = Files.newBufferedReader(PREFS_PATH, StandardCharsets.UTF_8)) {
            Map<UUID, String> loaded = GSON.fromJson(
                reader,
                new TypeToken<Map<UUID, String>>() {}.getType()
            );
            if (loaded != null) {
                this.preferences = loaded;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load player preferences", e);
        }
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(PREFS_PATH, StandardCharsets.UTF_8)) {
            GSON.toJson(preferences, writer);
        } catch (Exception e) {
            LOGGER.error("Failed to save player preferences", e);
        }
    }

    public String getPreference(UUID uuid) {
        return preferences.get(uuid);
    }

    public void setPreference(UUID uuid, String packName) {
        if (packName == null) {
            preferences.remove(uuid);
        } else {
            preferences.put(uuid, packName.toLowerCase());
        }
        save();
    }
}
