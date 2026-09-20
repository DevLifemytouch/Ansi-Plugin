package de.lifemytouch.ansi.friend;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FriendRepository {

    private final JavaPlugin javaPlugin;
    private final File file;
    private final YamlConfiguration config;

    public FriendRepository(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;

        if(!javaPlugin.getDataFolder().exists()) javaPlugin.getDataFolder().mkdirs();

        this.file = new File(javaPlugin.getDataFolder(), "friends.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveFriend(Friend friend) {
        String playerPath = "players." + friend.getPlayer();

        List<String> friends = config.getStringList(playerPath + ".friends");

        if (!friends.contains(friend.getFriend().toString())) friends.add(friend.getFriend().toString());

        config.set(playerPath + ".friends", friends);

        save();
    }

    public void removeFriend(UUID player, UUID friend) {
        String path = "players." + player + ".friends";

        List<String> friends = config.getStringList(path);

        friends.remove(friend.toString());
        config.set(path, friends);

        save();
    }

    public Set<UUID> getFriends(UUID player) {
        String path = "players." + player + ".friends";

        List<String> friends = config.getStringList(path);

        Set<UUID> result = new HashSet<>();

        for(String uuid : friends) {
            try {
                result.add(UUID.fromString(uuid));
            } catch (IllegalArgumentException ignored) {

            }
        }
        return result;
    }

    public void saveRequest(FriendRequest request) {
        String path = "players." + request.getReceiver() + ".requests";

        List<String> requests = config.getStringList(path);

        if(!requests.contains(request.getSender().toString())) requests.add(request.getSender().toString());

        config.set(path, requests);

        save();
    }

    public void removeRequest(UUID receiver, UUID sender) {
        String path = "players." + receiver + ".requests";

        List<String> requests = config.getStringList(path);

        requests.remove(sender.toString());

        config.set(path, requests);

        save();
    }

    public Set<UUID> getRequests(UUID player) {
        String path = "players." + player + ".requests";

        List<String> requests = config.getStringList(path);

        Set<UUID> result = new HashSet<>();

        for(String uuid : requests) {
            try {
                result.add(UUID.fromString(uuid));
            } catch (IllegalArgumentException ignored) {

            }
        }

        return result;
    }

    public FriendRequestSetting getRequestSetting(UUID player) {
        String value = config.getString(
                "players." + player + ".friend-request-setting",
                FriendRequestSetting.EVERYONE.name()
        );

        try {
            return FriendRequestSetting.valueOf(value);
        } catch (IllegalArgumentException exception) {
            return FriendRequestSetting.EVERYONE;
        }
    }

    public void setRequestSetting(
            UUID player,
            FriendRequestSetting setting
    ) {
        config.set(
                "players." + player + ".friend-request-setting",
                setting.name()
        );

        save();
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException exception) {
            javaPlugin.getLogger().severe("Konnte friends.yml nicht speichern!");
            exception.printStackTrace();
        }
    }

}
