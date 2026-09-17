package de.lifemytouch.ansi.cosmetic;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CosmeticRepository {

    private final JavaPlugin plugin;
    private final File file;
    private final YamlConfiguration config;

    public CosmeticRepository(JavaPlugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        this.file = new File(plugin.getDataFolder(), "cosmetics.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean hasCosmetic(UUID uuid, String cosmeticId) {
        return config.getBoolean(
                "players." + uuid + ".cosmetics." + cosmeticId,
                false
        );
    }

    public void setCosmetic(UUID uuid, String cosmeticId, boolean owned) {
        config.set(
                "players." + uuid + ".cosmetics." + cosmeticId,
                owned
        );

        save();
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe(
                    "Konnte cosmetics.yml nicht speichern: "
                            + exception.getMessage()
            );
        }
    }
}