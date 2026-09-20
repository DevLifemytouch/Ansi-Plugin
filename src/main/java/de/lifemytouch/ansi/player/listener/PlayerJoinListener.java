package de.lifemytouch.ansi.player.listener;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.playtime.PlaytimeService;
import de.lifemytouch.ansi.rank.RankManager;
import de.lifemytouch.ansi.server.scoreboard.ScoreboardListener;
import de.lifemytouch.ansi.server.scoreboard.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final RankManager rankManager;
    private final ScoreboardManager scoreboardManager;
    private final PlaytimeService playtimeService;

    public PlayerJoinListener(RankManager rankManager,
                              ScoreboardManager scoreboardManager, PlaytimeService playtimeService) {
        this.rankManager = rankManager;
        this.scoreboardManager = scoreboardManager;
        this.playtimeService = playtimeService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        rankManager.applyPermission(player);
        playtimeService.start(player);

        player.sendMessage(
                Messages.getPREFIX()
                        + "§7Willkommen auf "
                        + Messages.getANSI_GRADIENT()
                        + "'s §7Server!"
        );

        scoreboardManager.update(player);
        scoreboardManager.updateTabListForAll();
        scoreboardManager.updateScoreboardForAll();

        event.setJoinMessage("");
    }
}
