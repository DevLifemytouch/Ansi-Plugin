package de.lifemytouch.ansi.report;

import java.util.UUID;

public class Report {

    private final long id;
    private final UUID target;
    private final UUID reporter;
    private final ReportCategory category;
    private final String reason;
    private final long createdAt;

    private ReportStatus status;
    private UUID moderator;

    public Report(long id,
                  UUID target,
                  UUID reporter,
                  ReportCategory category,
                  String reason,
                  long createdAt,
                  ReportStatus status,
                  UUID moderator
    ) {
        this.id = id;
        this.target = target;
        this.reporter = reporter;
        this.category = category;
        this.reason = reason;
        this.createdAt = createdAt;
        this.status = status;
        this.moderator = moderator;
    }

    public long getId() {
        return id;
    }

    public UUID getTarget() {
        return target;
    }

    public UUID getReporter() {
        return reporter;
    }

    public ReportCategory getCategory() {
        return category;
    }

    public String getReason() {
        return reason;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public UUID getModerator() {
        return moderator;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public void setModerator(UUID moderator) {
        this.moderator = moderator;
    }
}
