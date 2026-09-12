package de.lifemytouch.ansi.rank;

import de.lifemytouch.ansi.server.tab.TabListManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class RankManager {
    private final JavaPlugin plugin;
    private final File dataFile;
    private final Consumer<Player> onRankApplied;

    private final Map<UUID, Rank> playerRanks = new HashMap<>();
    private final Map<UUID, PermissionAttachment> attachments = new HashMap<>();

    public RankManager(JavaPlugin plugin, Consumer<Player> onRankApplied) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "ranks.yml");
        this.onRankApplied = onRankApplied;
        load();
    }

    private void load() {
        if (!dataFile.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        if (config.getConfigurationSection("players") == null) return;

        for (String uuidString : config.getConfigurationSection("players").getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            Rank rank = Rank.fromName(config.getString("players." + uuidString));
            if (rank != null) {
                playerRanks.put(uuid, rank);
            }
        }
    }

    private void save() {
        FileConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, Rank> entry : playerRanks.entrySet()) {
            config.set("players." + entry.getKey(), entry.getValue().name());
        }
        try {
            config.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Konnte ranks.yml nicht speichern: " + e.getMessage());
        }
    }

    public void setRank(OfflinePlayer target, Rank rank) {
        playerRanks.put(target.getUniqueId(), rank);
        save();

        Player online = target.getPlayer();
        if (online != null) {
            applyPermission(online);
           if(onRankApplied != null) onRankApplied.accept(online);
        }
    }

    public Rank getRank(OfflinePlayer player) {
        return playerRanks.getOrDefault(player.getUniqueId(), Rank.DEFAULT);
    }

    public void applyPermission(Player player) {
        PermissionAttachment old = attachments.remove(player.getUniqueId());
        if (old != null) {
            player.removeAttachment(old);
        }

        Rank rank = getRank(player);
        PermissionAttachment attachment = player.addAttachment(plugin);
        attachment.setPermission(rank.getPermission(), true);

        for(String permission : rank.getPermissions()) {
            attachment.setPermission(permission, true);
        }

        attachments.put(player.getUniqueId(), attachment);
    }

    public void clearPermission(Player player) {
        PermissionAttachment attachment = attachments.remove(player.getUniqueId());
        if (attachment != null) {
            player.removeAttachment(attachment);
        }
    }
}
