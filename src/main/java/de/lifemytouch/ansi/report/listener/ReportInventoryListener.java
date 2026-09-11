package de.lifemytouch.ansi.report.listener;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.report.Report;
import de.lifemytouch.ansi.report.ReportCategory;
import de.lifemytouch.ansi.report.ReportService;
import de.lifemytouch.ansi.report.ReportStatus;
import de.lifemytouch.ansi.report.gui.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.UUID;

public class ReportInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(!(event.getWhoClicked() instanceof Player player)) return;

        if(!(event.getInventory().getHolder() instanceof ReportInventoryHolder inventoryHolder)) return;

        event.setCancelled(true);

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

    private void handleReportCreation(
            InventoryClickEvent event,
            Player player,
            ReportInventoryHolder inventoryHolder
    ) {
        if(event.getRawSlot() < 0 || event.getRawSlot() >= event.getView().getTopInventory().getSize()) return;

        ReportCategory reportCategory = switch (event.getRawSlot()) {
            case 10 -> ReportCategory.HACKING;
            case 12 -> ReportCategory.CHAT;
            case 14 -> ReportCategory.BUGUSING;
            case 16 -> ReportCategory.ADVERTISING;
            case 22 -> ReportCategory.OTHER;
            default -> null;
        };

        if(reportCategory == null) return;

        Player target = Bukkit.getPlayer(inventoryHolder.getTarget());

        if(target == null) {
            player.closeInventory();
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return;
        }

        String reason = switch (reportCategory) {
            case HACKING -> "Hacking / Cheating";
            case CHAT -> "Chat";
            case BUGUSING -> "Bugusing";
            case ADVERTISING -> "Werbung";
            case OTHER -> "Sonstiges";
        };

        inventoryHolder.getReportService().createReport(player, target, reportCategory, reason);

        player.closeInventory();

        player.sendMessage(Messages.getPREFIX() + "§7Dein Report gegen §6§l" +
                target.getName() +
                "§7 wurde vom Team empfangen und wird in kürze bearbeitet"
        );

    }

    private void handleReportList(
            InventoryClickEvent event,
            Player player,
            ReportListHolder holder
    ) {

        int slot = event.getRawSlot();

        if (slot == 49) {
            player.closeInventory();
            return;
        }

        if (slot == 45) {
            ReportListGUI.open(
                    player,
                    holder.getReportService(),
                    holder.getPage() - 1
            );
            return;
        }

        if (slot == 53) {
            ReportListGUI.open(
                    player,
                    holder.getReportService(),
                    holder.getPage() + 1
            );
            return;
        }

        if (slot < 0 || slot >= 45) {
            return;
        }

        List<Report> reports = new java.util.ArrayList<>();

        reports.addAll(
                holder.getReportService().getOpenReports()
        );

        reports.addAll(
                holder.getReportService().getReportsInReview()
        );

        reports.sort((first, second) ->
                Long.compare(
                        second.getId(),
                        first.getId()
                )
        );

        int index =
                holder.getPage() * 45 + slot;

        if (index >= reports.size()) {
            return;
        }

        Report report = reports.get(index);

        ReportDetailGUI.open(
                player,
                holder.getReportService(),
                report.getId()
        );

    }

    public void handleReportDetail(InventoryClickEvent event, Player player, ReportDetailHolder reportDetailHolder) {
        int slot = event.getRawSlot();

        if(slot == 22) {
            ReportListGUI.open(player, reportDetailHolder.getReportService(), 0);
            return;
        }

        Report report = reportDetailHolder.getReportService().getReport(reportDetailHolder.getReportId());

        if(report == null) {
            player.closeInventory();
            return;
        }

        switch (slot) {
            case 11 -> {
                player.sendMessage(Messages.getPREFIX() + "§cWIP");
            }

            case 13 -> {
                Player target = Bukkit.getPlayer(report.getTarget());

                if(target == null) {
                    player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
                    return;
                }

                player.teleport(target);
                player.sendMessage(Messages.getPREFIX() + "§7Du wurdest zu §6" + target.getName() + "§7teleportiert");
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

            }

            case 15 -> {

                if(report.getStatus() == ReportStatus.PENDING) {
                    reportDetailHolder.getReportService().takeReport(report.getId(), player.getUniqueId());

                    player.sendMessage(Messages.getPREFIX()
                            + "§7Report §6#" + report.getId() + "§7 wurde §eübernommen§f.");
                } else if(report.getStatus() == ReportStatus.IN_REVIEW) {
                    reportDetailHolder.getReportService().resolveReport(report.getId(), player.getUniqueId());
                    player.sendMessage(Messages.getPREFIX()
                            + "§7Report §6#" + report.getId() + "§7 wurde §aabgeschlossen§f.");
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
                }


                ReportDetailGUI.open(player, reportDetailHolder.getReportService(), report.getId());

            }

            case 16 -> {
                reportDetailHolder.getReportService().dismissReport(report.getId(), player.getUniqueId());

                player.sendMessage(Messages.getPREFIX() + "§7Report §6#" + report.getId() + " §7wurde §cabgelehnt§7.");

                ReportListGUI.open(player, reportDetailHolder.getReportService(), 0);
            }

            default -> {

            }

        }

    }

}
