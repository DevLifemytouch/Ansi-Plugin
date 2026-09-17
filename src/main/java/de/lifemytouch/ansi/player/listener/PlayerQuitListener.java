package de.lifemytouch.ansi.player.listener;

import de.lifemytouch.ansi.rank.RankManager;
import de.lifemytouch.ansi.report.ReportObservationService;
import de.lifemytouch.ansi.server.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerQuitListener implements Listener {

    private final JavaPlugin plugin;
    private final RankManager rankManager;
    private final ReportObservationService reportObservationService;
    private final ScoreboardManager scoreboardManager;

    public PlayerQuitListener(
            JavaPlugin plugin,
            RankManager rankManager,
            ReportObservationService reportObservationService,
            ScoreboardManager scoreboardManager
    ) {
        this.plugin = plugin;
        this.rankManager = rankManager;
        this.reportObservationService = reportObservationService;
        this.scoreboardManager = scoreboardManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        event.setQuitMessage("");

        reportObservationService.stop(player);
        rankManager.clearPermission(player);

        Bukkit.getScheduler().runTaskLater(
                plugin,
                () -> {
                    scoreboardManager.updateTabListForAll();
                    scoreboardManager.updateScoreboardForAll();
                },
                1L
        );
    }
}
