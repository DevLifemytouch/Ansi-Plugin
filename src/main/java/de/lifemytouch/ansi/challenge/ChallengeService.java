package de.lifemytouch.ansi.challenge;

import de.lifemytouch.ansi.challenge.item.ItemChallengeManager;
import de.lifemytouch.ansi.challenge.mob.MobChallengeManager;
import de.lifemytouch.ansi.timer.TimerManager;
import org.bukkit.entity.Player;

public class ChallengeService {

    private final ItemChallengeManager itemChallengeManager;
    private final MobChallengeManager mobChallengeManager;

    private Runnable beforeStart;

    public ChallengeService(
            ItemChallengeManager itemChallengeManager,
            MobChallengeManager mobChallengeManager,
            Runnable beforeStart
    ) {
        this.itemChallengeManager = itemChallengeManager;
        this.mobChallengeManager = mobChallengeManager;
        this.beforeStart = beforeStart;
    }

    public void start(Player player, ChallengeType type) {
        beforeStart.run();

        switch (type) {
            case ITEMS -> itemChallengeManager.start(player);
            case MOBS -> mobChallengeManager.start(player);
        }
    }

    public void stop() {
        itemChallengeManager.stop();
        mobChallengeManager.stop();
    }
}