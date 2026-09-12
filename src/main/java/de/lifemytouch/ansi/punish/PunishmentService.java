package de.lifemytouch.ansi.punish;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishmentService {

    private final PunishmentRepository punishmentRepository;

    public PunishmentService(PunishmentRepository punishmentRepository) {
        this.punishmentRepository = punishmentRepository;
    }

    public Punishment punish(
            UUID target,
            UUID moderator,
            PunishmentType punishmentType,
            PunishmentCategory punishmentCategory,
            String reason,
            Duration duration
    ) {
        Instant createdAt = Instant.now();

        Instant expiresAt =
                duration == null
                        ? null
                        : createdAt.plus(duration);

        Punishment punishment = new Punishment(
                UUID.randomUUID(),
                target,
                moderator,
                punishmentType,
                reason,
                createdAt,
                expiresAt,
                punishmentCategory
        );

        punishmentRepository.save(punishment);

        return punishment;
    }

    public Punishment ban(
            UUID target,
            UUID moderator,
            PunishmentCategory punishmentCategory,
            String reason,
            Duration duration
    ) {
        return punish(
                target,
                moderator,
                PunishmentType.BAN,
                punishmentCategory,
                reason,
                duration
        );
    }

    public Punishment mute(
            UUID target,
            UUID moderator,
            PunishmentCategory punishmentCategory,
            String reason,
            Duration duration
    ) {
        return punish(
                target,
                moderator,
                PunishmentType.MUTE,
                punishmentCategory,
                reason,
                duration
        );
    }

    public List<Punishment> getPunishments(UUID target) {
        List<Punishment> result = new ArrayList<>();

        for(Punishment punishment : punishmentRepository.findAll()) {
            if(!punishment.getTarget().equals(target)) continue;

            result.add(punishment);
        }

        return result;
    }

    public List<Punishment> getActivePunishments(UUID target) {
        List<Punishment> result = new ArrayList<>();

        for(Punishment punishment : getPunishments(target)) {
            if(punishment.isActive()) {
                result.add(punishment);
            }
        }

        return result;
    }

    public Punishment getActivePunishment(
            UUID target,
            PunishmentType punishmentType
    ) {
        for(Punishment punishment : getActivePunishments(target)) {
            if(punishment.getPunishmentType() == punishmentType) {
                return punishment;
            }
        }

        return null;
    }
}