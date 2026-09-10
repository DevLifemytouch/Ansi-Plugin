package de.lifemytouch.ansi.challenge;

import de.lifemytouch.ansi.challenge.item.ItemChallengeManager;
import de.lifemytouch.ansi.challenge.mob.MobChallengeManager;
import de.lifemytouch.ansi.timer.TimerManager;
import org.bukkit.entity.Player;

public class ChallengeService {

    private final TimerManager timerManager;
    private final ItemChallengeManager itemChallengeManager;
    private final MobChallengeManager mobChallengeManager;

    public ChallengeService(
            TimerManager timerManager,
            ItemChallengeManager itemChallengeManager,
            MobChallengeManager mobChallengeManager
    ) {
        this.timerManager = timerManager;
        this.itemChallengeManager = itemChallengeManager;
        this.mobChallengeManager = mobChallengeManager;
    }

    public void start(Player player, ChallengeType type) {
        timerManager.reset();

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