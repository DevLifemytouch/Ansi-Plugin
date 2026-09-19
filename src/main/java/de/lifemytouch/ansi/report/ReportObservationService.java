package de.lifemytouch.ansi.report;

import de.lifemytouch.ansi.vanish.VanishService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportObservationService {

    private final VanishService vanishService;
    private final Map<UUID, Observation> observations = new HashMap<>();

    public ReportObservationService(VanishService vanishService) {
        this.vanishService = vanishService;
    }

    public boolean start(Player moderator, Player target) {
        if (moderator.equals(target) || isObserving(moderator)) {
            return false;
        }

        boolean wasAlreadyVanished = vanishService.isVanished(moderator);
        Location returnLocation = moderator.getLocation().clone();

        observations.put(
                moderator.getUniqueId(),
                new Observation(
                        target.getUniqueId(),
                        returnLocation,
                        wasAlreadyVanished
                )
        );

        if (!wasAlreadyVanished) {
            vanishService.setVanish(moderator, true);
        }

        moderator.teleport(target);
        return true;
    }

    public boolean stop(Player moderator) {
        Observation observation = observations.remove(moderator.getUniqueId());

        if (observation == null) {
            return false;
        }

        if (!observation.wasAlreadyVanished()) {
            vanishService.setVanish(moderator, false);
        }

        Location returnLocation = observation.returnLocation();

        if (returnLocation.getWorld() != null) {
            moderator.teleport(returnLocation);
        }

        return true;
    }

    public boolean isObserving(Player moderator) {
        return observations.containsKey(moderator.getUniqueId());
    }

    public UUID getTarget(Player moderator) {
        Observation observation = observations.get(moderator.getUniqueId());
        return observation == null ? null : observation.target();
    }

    public Player getTargetPlayer(Player moderator) {
        UUID target = getTarget(moderator);
        return target == null ? null : Bukkit.getPlayer(target);
    }
}