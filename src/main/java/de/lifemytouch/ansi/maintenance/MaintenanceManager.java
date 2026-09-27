package de.lifemytouch.ansi.maintenance;

import de.lifemytouch.ansi.rank.Rank;
import de.lifemytouch.ansi.rank.RankManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MaintenanceManager {

    private final JavaPlugin plugin;
    private final RankManager rankManager;
    private final File dataFile;

    private boolean enabled;
    private final Set<UUID> allowedPlayers = new HashSet<>();

    public MaintenanceManager(
            JavaPlugin plugin,
            RankManager rankManager
    ) {
        this.plugin = plugin;
        this.rankManager = rankManager;
        this.dataFile = new File(plugin.getDataFolder(), "maintenance.yml");

        load();
    }

    private void load() {

        if (!dataFile.exists()) {
            return;
        }

        FileConfiguration config =
                YamlConfiguration.loadConfiguration(dataFile);

        enabled = config.getBoolean("enabled", false);

        for (String uuidString : config.getStringList("players")) {
            try {
                allowedPlayers.add(UUID.fromString(uuidString));
            } catch (IllegalArgumentException ignored) {
                plugin.getLogger().warning(
                        "Ungültige UUID in maintenance.yml: " + uuidString
                );
            }
        }
    }

    private void save() {

        FileConfiguration config = new YamlConfiguration();

        config.set("enabled", enabled);

        config.set(
                "players",
                allowedPlayers.stream()
                        .map(UUID::toString)
                        .toList()
        );

        try {
            config.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe(
                    "Konnte maintenance.yml nicht speichern: "
                            + e.getMessage()
            );
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        save();
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public boolean addPlayer(OfflinePlayer player) {
        boolean added = allowedPlayers.add(player.getUniqueId());

        if (added) {
            save();
        }

        return added;
    }

    public boolean removePlayer(OfflinePlayer player) {
        boolean removed = allowedPlayers.remove(player.getUniqueId());

        if (removed) {
            save();
        }

        return removed;
    }

    public boolean isAllowed(Player player) {

        Rank rank = rankManager.getRank(player);

        if (rank == Rank.OWNER
                || rank == Rank.DEV
                || rank == Rank.ADMIN) {
            return true;
        }

        return allowedPlayers.contains(player.getUniqueId());
    }

    public Set<UUID> getAllowedPlayers() {
        return Set.copyOf(allowedPlayers);
    }
}