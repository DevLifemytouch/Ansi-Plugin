package de.lifemytouch.ansi.player.listener;

import de.lifemytouch.ansi.challenge.item.ItemChallengeManager;
import de.lifemytouch.ansi.challenge.mob.MobChallengeManager;
import de.lifemytouch.ansi.rank.RankManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final RankManager rankManager;
    private final ItemChallengeManager itemChallengeManager;
    private final MobChallengeManager mobChallengeManager;

    public PlayerQuitListener(RankManager rankManager, ItemChallengeManager itemChallengeManager,
                              MobChallengeManager mobChallengeManager) {
        this.rankManager = rankManager;
        this.itemChallengeManager = itemChallengeManager;
        this.mobChallengeManager = mobChallengeManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage("");

        rankManager.clearPermission(player);
        itemChallengeManager.removePlayer(player);
        mobChallengeManager.removePlayer(player);
    }

}
