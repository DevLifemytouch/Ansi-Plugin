package de.lifemytouch.ansi.message;

import de.lifemytouch.ansi.friend.FriendService;

import java.util.UUID;

public class PrivateMessageService {

    private final PrivateMessageRepository repository;
    private final FriendService friendService;

    public PrivateMessageService(
            PrivateMessageRepository repository,
            FriendService friendService
    ) {
        this.repository = repository;
        this.friendService = friendService;
    }

    public PrivateMessageSetting getSetting(UUID playerId) {
        return repository.getSetting(playerId);
    }

    public PrivateMessageSetting cycleSetting(UUID playerId) {
        PrivateMessageSetting next = getSetting(playerId).next();

        repository.setSetting(playerId, next);
        return next;
    }

    public boolean canReceive(UUID receiverId, UUID senderId) {
        return switch (getSetting(receiverId)) {
            case EVERYONE -> true;
            case FRIENDS_ONLY -> friendService.areFriends(receiverId, senderId);
            case NOBODY -> false;
        };
    }
}