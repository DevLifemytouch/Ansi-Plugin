package de.lifemytouch.ansi.challenge.mob;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobChallengeListener implements Listener {

    private final MobChallengeManager mobChallengeManager;

    public MobChallengeListener(MobChallengeManager mobChallengeManager) {
        this.mobChallengeManager = mobChallengeManager;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        mobChallengeManager.onMobKilled(event.getEntityType(), killer);
    }
}