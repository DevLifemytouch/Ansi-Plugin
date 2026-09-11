package de.lifemytouch.ansi.punish;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishmentRepository {

    private final JavaPlugin plugin;
    private final File file;
    private final YamlConfiguration configuration;

    public PunishmentRepository(JavaPlugin plugin) {
        this.plugin = plugin;

        this.file = new File(
                plugin.getDataFolder(),
                "punishments.yml"
        );

        if(!file.exists()) {
            try {
                if(!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                file.createNewFile();
            } catch(IOException exception) {
                plugin.getLogger().severe(
                        "Punishments-Datei konnte nicht erstellt werden!"
                );
                exception.printStackTrace();
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void save(Punishment punishment) {

        String path = "punishments." + punishment.getId();

        configuration.set(
                path + ".target",
                punishment.getTarget().toString()
        );

        configuration.set(
                path + ".moderator",
                punishment.getModerator().toString()
        );

        configuration.set(
                path + ".type",
                punishment.getPunishmentType().name()
        );

        configuration.set(
                path + ".reason",
                punishment.getReason()
        );

        configuration.set(
                path + ".createdAt",
                punishment.getCreatedAt().toString()
        );

        configuration.set(
                path + ".expiresAt",
                punishment.getExpiresAt() == null
                        ? null
                        : punishment.getExpiresAt().toString()
        );

        saveFile();
    }

    public List<Punishment> findAll() {

        List<Punishment> punishments = new ArrayList<>();

        ConfigurationSection section =
                configuration.getConfigurationSection("punishments");

        if(section == null) {
            return punishments;
        }

        for(String id : section.getKeys(false)) {

            Punishment punishment = load(id);

            if(punishment != null) {
                punishments.add(punishment);
            }
        }

        return punishments;
    }

    public Punishment load(String id) {

        String path = "punishments." + id;

        if(!configuration.contains(path)) {
            return null;
        }

        try {
            return new Punishment(
                    UUID.fromString(id),
                    UUID.fromString(
                            configuration.getString(path + ".target")
                    ),
                    UUID.fromString(
                            configuration.getString(path + ".moderator")
                    ),
                    PunishmentType.valueOf(
                            configuration.getString(path + ".type")
                    ),
                    configuration.getString(path + ".reason"),
                    Instant.parse(
                            configuration.getString(path + ".createdAt")
                    ),
                    configuration.getString(path + ".expiresAt") == null
                            ? null
                            : Instant.parse(
                            configuration.getString(
                                    path + ".expiresAt"
                            )
                    )
            );
        } catch(Exception exception) {
            plugin.getLogger().warning(
                    "Punishment konnte nicht geladen werden: " + id
            );
            return null;
        }
    }

    private void saveFile() {
        try {
            configuration.save(file);
        } catch(IOException exception) {
            plugin.getLogger().severe(
                    "Punishments-Datei konnte nicht gespeichert werden!"
            );
            exception.printStackTrace();
        }
    }
}