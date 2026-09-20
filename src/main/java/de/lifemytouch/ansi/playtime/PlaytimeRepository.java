package de.lifemytouch.ansi.playtime;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlaytimeRepository {

    private final JavaPlugin plugin;
    private final File file;
    private final YamlConfiguration config;

    public PlaytimeRepository(JavaPlugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        file = new File(plugin.getDataFolder(), "playtime.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    public long getPlaytime(UUID playerId) {
        return config.getLong("players." + playerId + ".milliseconds", 0L);
    }

    public void addPlaytime(UUID playerId, long milliseconds) {
        if (milliseconds <= 0) {
            return;
        }

        long current = getPlaytime(playerId);
        long updated = current > Long.MAX_VALUE - milliseconds
                ? Long.MAX_VALUE
                : current + milliseconds;

        config.set("players." + playerId + ".milliseconds", updated);
        save();
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe(
                    "Konnte playtime.yml nicht speichern: "
                            + exception.getMessage()
            );
        }
    }
}