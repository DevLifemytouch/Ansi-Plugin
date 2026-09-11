package de.lifemytouch.ansi.punish;

import java.time.Instant;
import java.util.UUID;

public class Punishment {

    private final UUID id;
    private final UUID target;
    private final UUID moderator;
    private final PunishmentType punishmentType;
    private final String reason;
    private final Instant createdAt;
    private final Instant expiresAt;

    public Punishment(UUID id, UUID target, UUID moderator,
                      PunishmentType punishmentType, String reason,
                      Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.target = target;
        this.moderator = moderator;
        this.punishmentType = punishmentType;
        this.reason = reason;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTarget() {
        return target;
    }

    public UUID getModerator() {
        return moderator;
    }

    public PunishmentType getPunishmentType() {
        return punishmentType;
    }

    public String getReason() {
        return reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isPermanent() {
        return expiresAt == null;
    }

    public boolean isExpired() {
        return expiresAt != null && !expiresAt.isAfter(Instant.now());
    }

    public boolean isActive() {
        return !isExpired();
    }

}
