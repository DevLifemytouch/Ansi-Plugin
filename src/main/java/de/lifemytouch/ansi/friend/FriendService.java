package de.lifemytouch.ansi.friend;

import java.util.Set;
import java.util.UUID;

public class FriendService {

    private final FriendRepository repository;

    public FriendService(FriendRepository repository) {
        this.repository = repository;
    }

    public boolean areFriends(UUID player, UUID friend) {
        return repository.getFriends(player).contains(friend);
    }

    public boolean addFriend(UUID player, UUID friend) {
        if(player.equals(friend)) return false;
        if(areFriends(player, friend)) return false;

        Friend friendship = new Friend(player, friend);
        repository.saveFriend(friendship);

        Friend reverseFriendship = new Friend(friend, player);
        repository.saveFriend(reverseFriendship);

        return true;
    }

    public boolean removeFriend(UUID player, UUID friend) {
        if(!areFriends(player, friend)) return false;

        repository.removeFriend(player, friend);
        repository.removeFriend(friend, player);

        return true;
    }

    public Set<UUID> getFriends(UUID player) {
        return repository.getFriends(player);
    }

    public boolean hasPendingRequest(UUID receiver, UUID sender) {
        return repository.getRequests(receiver).contains(sender);
    }

    public boolean sendRequest(UUID sender, UUID receiver) {
        if (sender.equals(receiver)) return false;
        if(areFriends(sender, receiver)) return false;
        if(hasPendingRequest(receiver, sender)) return false;

        FriendRequest request = new FriendRequest(sender, receiver);

        repository.saveRequest(request);

        return true;
    }

    public boolean acceptRequest(UUID receiver, UUID sender) {
        if(!hasPendingRequest(receiver, sender)) return false;

        repository.removeRequest(receiver, sender);

        return addFriend(receiver, sender);
    }

    public boolean denyRequest(UUID receiver, UUID sender) {
        if (!hasPendingRequest(receiver, sender)) return false;

        repository.removeRequest(receiver, sender);

        return true;
    }

    public Set<UUID> getRequests(UUID player) {
        return repository.getRequests(player);
    }

}
