package de.lifemytouch.ansi.report;

public enum ReportPriority {

    HIGH("§cHoch", 0),
    MEDIUM("§6Mittel", 1),
    LOW("§aNiedrig", 2);

    private final String displayName;
    private final int sortOrder;

    ReportPriority(String displayName, int sortOrder) {
        this.displayName = displayName;
        this.sortOrder = sortOrder;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public static ReportPriority fromCategory(ReportCategory category) {
        return switch (category) {
            case HACKING, BUGUSING -> HIGH;
            case CHAT, ADVERTISING -> MEDIUM;
            case OTHER -> LOW;
        };
    }
}