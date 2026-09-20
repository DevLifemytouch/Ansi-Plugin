package de.lifemytouch.ansi.friend;

public enum FriendRequestSetting {

    EVERYONE("§aJeder"),
    NOBODY("§cNiemand");

    private final String displayName;

    FriendRequestSetting(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public FriendRequestSetting next() {
        return this == EVERYONE ? NOBODY : EVERYONE;
    }
}