package de.lifemytouch.ansi.rank;

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
    private final Map<UUID, Long> rankExpirations = new HashMap<>();
    private final Map<UUID, Rank> previousRanks = new HashMap<>();

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
            String path = "players." + uuidString;

            if (config.isConfigurationSection(path)) {
                Rank rank = Rank.fromName(config.getString(path + ".rank"));

                if (rank != null) playerRanks.put(uuid, rank);

                long expires = config.getLong(path + ".expires", -1);

                if (expires != -1) rankExpirations.put(uuid, expires);

                Rank previousRank = Rank.fromName(config.getString(path + ".previous-rank"));

                if (previousRank != null) previousRanks.put(uuid, previousRank);

            } else {
                Rank rank = Rank.fromName(config.getString(path));

                if (rank != null) playerRanks.put(uuid, rank);
            }
        }
    }

    private void save() {
        FileConfiguration config = new YamlConfiguration();

        for (Map.Entry<UUID, Rank> entry : playerRanks.entrySet()) {
            UUID uuid = entry.getKey();
            Rank rank = entry.getValue();
            String path = "players." + uuid;

            config.set(path + ".rank", rank.name());

            long expires = rankExpirations.getOrDefault(uuid, -1L);
            config.set(path + ".expires", expires);

            Rank previousRank = previousRanks.get(uuid);

            if (previousRank != null) {
                config.set(path + ".previous-rank", previousRank.name());
            }
        }

        try {
            config.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe(
                    "Konnte ranks.yml nicht speichern: " + e.getMessage()
            );
        }
    }

    public void setRank(OfflinePlayer target, Rank rank) {
        UUID uuid = target.getUniqueId();

        playerRanks.put(uuid, rank);
        rankExpirations.remove(uuid);
        previousRanks.remove(uuid);

        save();

        Player online = target.getPlayer();

        if (online != null) {
            applyPermission(online);

            if (onRankApplied != null) onRankApplied.accept(online);
        }
    }

    public void setTemporaryRank(OfflinePlayer target, Rank rank, long duration) {

        UUID uuid = target.getUniqueId();

        Rank currentRank = getRank(target);

        previousRanks.put(uuid, currentRank);
        playerRanks.put(uuid, rank);

        long expiresAt = System.currentTimeMillis() + duration;
        rankExpirations.put(uuid, expiresAt);

        save();

        Player online = target.getPlayer();

        if (online != null) {
            applyPermission(online);
            if (onRankApplied != null) {
                onRankApplied.accept(online);
            }
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

    public long getRankExpiration(OfflinePlayer player) {
        return rankExpirations.getOrDefault(
                player.getUniqueId(),
                -1L
        );
    }

    public boolean hasTemporaryRank(OfflinePlayer player) {
        return rankExpirations.containsKey(player.getUniqueId());
    }

    public void expireRank(OfflinePlayer target) {

        UUID uuid = target.getUniqueId();

        Rank previousRank = previousRanks.getOrDefault(
                uuid,
                Rank.DEFAULT
        );

        playerRanks.put(uuid, previousRank);

        rankExpirations.remove(uuid);
        previousRanks.remove(uuid);

        save();

        Player online = target.getPlayer();

        if (online != null) {
            applyPermission(online);

            if (onRankApplied != null) {
                onRankApplied.accept(online);
            }
        }
    }

    public void checkExpiredRanks() {
        long now = System.currentTimeMillis();

        for (UUID uuid : new HashMap<>(rankExpirations).keySet()) {
            long expiresAt = rankExpirations.get(uuid);
            if (now >= expiresAt) {
                OfflinePlayer player = plugin.getServer().getOfflinePlayer(uuid);
                expireRank(player);
                plugin.getLogger().info(
                        "Temporärer Rang von " + player.getName() + " ist abgelaufen."
                );
            }
        }
    }
}
