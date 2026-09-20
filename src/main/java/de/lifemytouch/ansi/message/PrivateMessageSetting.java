package de.lifemytouch.ansi.message;

public enum PrivateMessageSetting {

    EVERYONE("§aJeder"),
    FRIENDS_ONLY("§eNur Freunde"),
    NOBODY("§cNiemand");

    private final String displayName;

    PrivateMessageSetting(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public PrivateMessageSetting next() {
        return switch (this) {
            case EVERYONE -> FRIENDS_ONLY;
            case FRIENDS_ONLY -> NOBODY;
            case NOBODY -> EVERYONE;
        };
    }
}