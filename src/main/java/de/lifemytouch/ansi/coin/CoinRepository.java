package de.lifemytouch.ansi.coin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CoinRepository {

    private final JavaPlugin plugin;
    private final File file;
    private final YamlConfiguration config;

    public CoinRepository(JavaPlugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        this.file = new File(plugin.getDataFolder(), "coins.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public long getCoins(UUID uuid) {
        return config.getLong(
                "players." + uuid + ".coins",
                0L
        );
    }

    public void setCoins(UUID uuid, long amount) {
        config.set(
                "players." + uuid + ".coins",
                amount
        );

        save();
    }

    public boolean isHidden(UUID uuid) {
        return config.getBoolean(
                "players." + uuid + ".hidden",
                false
        );
    }

    public void setHidden(UUID uuid, boolean hidden) {
        config.set(
                "players." + uuid + ".hidden",
                hidden
        );

        save();
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe(
                    "Konnte coins.yml nicht speichern: "
                            + exception.getMessage()
            );
        }
    }

}
