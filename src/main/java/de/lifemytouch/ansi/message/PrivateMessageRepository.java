package de.lifemytouch.ansi.message;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PrivateMessageRepository {

    private final JavaPlugin plugin;
    private final File file;
    private final YamlConfiguration config;

    public PrivateMessageRepository(JavaPlugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        this.file = new File(plugin.getDataFolder(), "private-messages.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public PrivateMessageSetting getSetting(UUID playerId) {
        String value = config.getString(
                "players." + playerId + ".setting",
                PrivateMessageSetting.EVERYONE.name()
        );

        try {
            return PrivateMessageSetting.valueOf(value);
        } catch (IllegalArgumentException exception) {
            return PrivateMessageSetting.EVERYONE;
        }
    }

    public void setSetting(UUID playerId, PrivateMessageSetting setting) {
        config.set("players." + playerId + ".setting", setting.name());

        try {
            config.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe(
                    "Konnte private-messages.yml nicht speichern: "
                            + exception.getMessage()
            );
        }
    }
}