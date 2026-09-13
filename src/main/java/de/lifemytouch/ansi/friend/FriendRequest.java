package de.lifemytouch.ansi.friend;

import java.util.UUID;

public class FriendRequest {

    private final UUID sender;
    private final UUID receiver;

    public FriendRequest(UUID sender, UUID receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public boolean isFor(UUID player) {
        return receiver.equals(player);
    }

    public boolean isFrom(UUID player) {
        return sender.equals(player);
    }
}
