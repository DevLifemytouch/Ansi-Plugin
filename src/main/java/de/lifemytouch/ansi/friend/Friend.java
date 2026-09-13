package de.lifemytouch.ansi.friend;

import java.util.UUID;

public class Friend {

    private final UUID player;
    private final UUID friend;

    public Friend(UUID player, UUID friend) {
        this.player = player;
        this.friend = friend;
    }

    public UUID getPlayer() {
        return player;
    }

    public UUID getFriend() {
        return friend;
    }

    public UUID getOtherPlayer(UUID uuid) {
        if(player.equals(uuid)) return friend;

        if(friend.equals(uuid)) return player;

        return null;
    }
}
