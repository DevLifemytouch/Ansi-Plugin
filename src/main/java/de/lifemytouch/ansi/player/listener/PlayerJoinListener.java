package de.lifemytouch.ansi.player.listener;

import de.lifemytouch.ansi.Ansi;
import de.lifemytouch.ansi.challenge.item.ItemChallengeManager;
import de.lifemytouch.ansi.challenge.mob.MobChallengeManager;
import de.lifemytouch.ansi.rank.RankManager;
import de.lifemytouch.ansi.server.tab.TabListManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final RankManager rankManager;
    private final ItemChallengeManager itemChallengeManager;
    private final MobChallengeManager mobChallengeManager;

    public PlayerJoinListener(RankManager rankManager, ItemChallengeManager itemChallengeManager,
                              MobChallengeManager mobChallengeManager) {
        this.rankManager = rankManager;
        this.itemChallengeManager = itemChallengeManager;
        this.mobChallengeManager = mobChallengeManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        rankManager.applyPermission(player);
        TabListManager.updatePrefix(player);
        itemChallengeManager.addPlayer(player);
        mobChallengeManager.addPlayer(player);

        player.sendMessage(Ansi.getPREFIX() + "§7Willkommen auf " + Ansi.getAnsiGradient() + "'s §7Server!");
        event.setJoinMessage("");
    }
}
