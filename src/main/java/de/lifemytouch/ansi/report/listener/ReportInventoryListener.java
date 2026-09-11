package de.lifemytouch.ansi.report.listener;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.report.ReportCategory;
import de.lifemytouch.ansi.report.ReportService;
import de.lifemytouch.ansi.report.gui.ReportInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class ReportInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(!(event.getInventory().getHolder() instanceof ReportInventoryHolder inventoryHolder)) return;

        event.setCancelled(true);

        if(!(event.getWhoClicked() instanceof Player player)) return;

        int slot = event.getRawSlot();

        ReportCategory reportCategory = switch (slot) {
            case 10 -> ReportCategory.HACKING;
            case 12 -> ReportCategory.CHAT;
            case 14 -> ReportCategory.BUGUSING;
            case 16 -> ReportCategory.ADVERTISING;
            case 22 -> ReportCategory.OTHER;
            default -> null;
        };

        if(reportCategory == null) return;

        UUID targetUUID = inventoryHolder.getTarget();

        Player target = Bukkit.getPlayer(targetUUID);

        if(target == null) {
            player.closeInventory();
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return;
        }

        ReportService reportService = inventoryHolder.getReportService();

        String reason = switch (reportCategory) {
            case HACKING -> "Hacking / Cheating";
            case CHAT -> "CHAT";
            case BUGUSING -> "Bugusing";
            case ADVERTISING -> "Werbung";
            case OTHER -> "Sonstiges";
            default -> null;
        };

        reportService.createReport(player, target, reportCategory, reason);

        player.closeInventory();

        player.sendMessage(Messages.getPREFIX() + "§7Dein Report gegen §6§l" +
                target.getName() +
                "§7 wurde vom Team empfangen und wird in kürze bearbeitet"
        );

    }

}
