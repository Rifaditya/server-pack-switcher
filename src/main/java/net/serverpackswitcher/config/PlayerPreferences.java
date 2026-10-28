// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3

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
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerPreferences {
    private static final Logger LOGGER = LoggerFactory.getLogger("serverpackswitcher");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Object FILE_LOCK = new Object();

    private static Path customPrefsPath = null;

    public static void setCustomPrefsPath(Path path) {
        synchronized (FILE_LOCK) {
            customPrefsPath = path;
        }
    }

    public static Path getPrefsPath() {
        synchronized (FILE_LOCK) {
            if (customPrefsPath != null) {
                return customPrefsPath;
            }
        }
        try {
            if (FabricLoader.getInstance() != null && FabricLoader.getInstance().getConfigDir() != null) {
                return FabricLoader.getInstance().getConfigDir().resolve("serverpackswitcher_preferences.json");
            }
        } catch (Throwable ignored) {
        }
        return Path.of("config", "serverpackswitcher_preferences.json");
    }

    private volatile Map<UUID, String> preferences = new ConcurrentHashMap<>();

    public synchronized void load() {
        synchronized (FILE_LOCK) {
            Path path = getPrefsPath();
            if (!Files.exists(path)) {
                preferences = new ConcurrentHashMap<>();
                return;
            }

            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                Map<UUID, String> loaded = GSON.fromJson(
                    reader,
                    new TypeToken<Map<UUID, String>>() {}.getType()
                );
                if (loaded != null) {
                    this.preferences = new ConcurrentHashMap<>(loaded);
                }
            } catch (Exception e) {
                LOGGER.error("Failed to load player preferences", e);
            }
        }
    }

    public synchronized void save() {
        synchronized (FILE_LOCK) {
            Path path = getPrefsPath();
            try {
                if (path.getParent() != null) {
                    Files.createDirectories(path.getParent());
                }
                try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                    GSON.toJson(preferences, writer);
                }
            } catch (Exception e) {
                LOGGER.error("Failed to save player preferences", e);
            }
        }
    }

    public String getPreference(UUID uuid) {
        return preferences.get(uuid);
    }

    public Map<UUID, String> getPreferences() {
        return Collections.unmodifiableMap(preferences);
    }

    public synchronized void setPreference(UUID uuid, String packName) {
        if (packName == null) {
            preferences.remove(uuid);
        } else {
            preferences.put(uuid, packName.toLowerCase());
        }
        save();
    }
}
