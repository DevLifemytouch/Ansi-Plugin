package de.lifemytouch.ansi.report;

import de.lifemytouch.ansi.vanish.VanishService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.BatchUpdateException;
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

        if (moderator.equals(target)) return false;

        if (isObserving(moderator)) return false;

        boolean wasAlreadyVanished = vanishService.isVanished(moderator);

        observations.put(moderator.getUniqueId(), new Observation(target.getUniqueId(), wasAlreadyVanished));

        if(!wasAlreadyVanished) {
            vanishService.setVanish(moderator, true);
        }

        moderator.teleport(target);

        return true;
    }

    public boolean stop(Player moderator) {
        Observation observation = observations.remove(moderator.getUniqueId());

        if(observation == null) return false;

        if(!observation.wasAlreadyVanished()) vanishService.setVanish(moderator, false);

        return true;
    }

    public boolean isObserving(Player moderator) {
        return observations.containsKey(moderator.getUniqueId());
    }

    public UUID getTarget(Player moderator) {
        Observation observation = observations.get(moderator.getUniqueId());

        if(observation == null) return null;

        return observation.target();
    }

    public Player getTargetPlayer(Player moderator) {
        UUID target = getTarget(moderator);

        if(target == null) return null;

        return Bukkit.getPlayer(target);
    }

}
