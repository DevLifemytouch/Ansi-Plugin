package de.lifemytouch.ansi.player.listener;

import de.lifemytouch.ansi.rank.RankManager;
import de.lifemytouch.ansi.report.ReportObservationService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final RankManager rankManager;
    private final ReportObservationService reportObservationService;

    public PlayerQuitListener(RankManager rankManager, ReportObservationService reportObservationService) {
        this.rankManager = rankManager;
        this.reportObservationService = reportObservationService;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage("");

        reportObservationService.stop(player);
        rankManager.clearPermission(player);
    }

}
