package de.lifemytouch.ansi.report.listener;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.punish.DurationParser;
import de.lifemytouch.ansi.punish.PunishmentCategory;
import de.lifemytouch.ansi.punish.PunishmentService;
import de.lifemytouch.ansi.punish.PunishmentType;
import de.lifemytouch.ansi.report.*;
import de.lifemytouch.ansi.report.gui.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.time.Duration;
import java.util.List;

public class ReportInventoryListener implements Listener {

    private final ReportObservationService reportObservationService;
    private final PunishmentService punishmentService;

    public ReportInventoryListener(
            ReportObservationService reportObservationService,
            PunishmentService punishmentService
    ) {
        this.reportObservationService = reportObservationService;
        this.punishmentService = punishmentService;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(!(event.getWhoClicked() instanceof Player player)) return;

        if(event.getView().getTopInventory().getHolder() instanceof ReportInventoryHolder holder) {
            event.setCancelled(true);

            handleReportCreation(event, player, holder);
            return;
        }

        if(event.getView().getTopInventory().getHolder() instanceof ReportListHolder holder) {
            event.setCancelled(true);

            handleReportList(event, player, holder);
            return;
        }

        if(event.getView().getTopInventory().getHolder() instanceof ReportDetailHolder holder) {
            event.setCancelled(true);

            handleReportDetail(event, player, holder);
            return;
        }

        if(event.getView().getTopInventory().getHolder() instanceof ReportPunishmentHolder holder) {
            event.setCancelled(true);

            handleReportPunishment(event, player, holder);
            return;
        }

        if(event.getView().getTopInventory().getHolder() instanceof ReportPunishmentTypeHolder holder) {
            event.setCancelled(true);

            handleReportPunishmentType(event, player, holder);
            return;
        }

        if(event.getView().getTopInventory().getHolder() instanceof ReportPunishmentDurationHolder holder) {
            event.setCancelled(true);

            handleReportPunishmentDuration(event, player, holder);
            return;
        }

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

        for(Player players : Bukkit.getOnlinePlayers()) {
            if(players.hasPermission("ansi.reports.handle")) {
                players.sendMessage(Messages.getPREFIX() + "§7Ein neuer §6Report §7ist eingegangen. ");
                players.sendMessage(Messages.getPREFIX() + "§6/reports §7zum bearbeiten. ");
            }
        }

    }

    private void handleReportList(
            InventoryClickEvent event,
            Player player,
            ReportListHolder holder
    ) {

        int slot = event.getRawSlot();

        if(slot == 46) {
            ReportFilter nextFilter = switch (holder.getReportFilter()) {
                case OPEN -> ReportFilter.PENDING;
                case PENDING -> ReportFilter.IN_REVIEW;
                case IN_REVIEW -> ReportFilter.RESOLVED;
                case RESOLVED -> ReportFilter.DISMISSED;
                case DISMISSED -> ReportFilter.ALL;
                case ALL -> ReportFilter.OPEN;
            };

            ReportListGUI.open(player, holder.getReportService(), 0, nextFilter);

        }

        if (slot == 49) {
            player.closeInventory();
            return;
        }

        if (slot == 45) {
            ReportListGUI.open(
                    player,
                    holder.getReportService(),
                    holder.getPage() - 1,
                    holder.getReportFilter()
            );
            return;
        }

        if (slot == 53) {
            ReportListGUI.open(
                    player,
                    holder.getReportService(),
                    holder.getPage() + 1,
                    holder.getReportFilter()
            );
            return;
        }

        if (slot < 0 || slot >= 45) {
            return;
        }

        List<Report> reports = ReportListGUI.getReports(
                holder.getReportService(),
                holder.getReportFilter()
        );

        int index = holder.getPage() * 45 + slot;

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
            ReportListGUI.open(player, reportDetailHolder.getReportService(),
                    0, reportDetailHolder.getReportFilter());
            return;
        }

        Report report = reportDetailHolder.getReportService().getReport(reportDetailHolder.getReportId());

        if(report == null) {
            player.closeInventory();
            return;
        }

        switch (slot) {
            case 10 -> {
                Player target = Bukkit.getPlayer(report.getTarget());

                if(target == null) {
                    player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
                    return;
                }

                if(report.getStatus() == ReportStatus.PENDING) {
                    reportDetailHolder.getReportService().takeReport(report.getId(), player.getUniqueId());
                }

                boolean started = reportObservationService.start(player, target);

                if(!started) {
                    player.sendMessage(Messages.getPREFIX() + "§7Du beobachtest bereits einen Spieler!");
                    return;
                }

                player.closeInventory();

                player.sendMessage(Messages.getPREFIX() + "§7Du beobachtest nun §6" + target.getName() + "§7.");

                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            }

            case 11 -> {
                Player target = Bukkit.getPlayer(report.getTarget());

                if(target == null) {
                    player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
                    return;
                }

                player.teleport(target);
                player.sendMessage(Messages.getPREFIX() + "§7Du wurdest zu §6" + target.getName() + "§7teleportiert");
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

            }

            case 13 -> {
                ReportPunishmentGUI.open(player, reportDetailHolder.getReportService(), report.getId());
            }

            case 15 -> {

                if(report.getStatus() == ReportStatus.PENDING) {
                    Player reporter = Bukkit.getPlayer(report.getReporter());
                    Player reported = Bukkit.getPlayer(report.getTarget());

                    reportDetailHolder.getReportService().takeReport(report.getId(), player.getUniqueId());

                    player.sendMessage(Messages.getPREFIX()
                            + "§7Report §6#" + report.getId() + "§7 wurde §eübernommen§f.");

                    reporter.sendMessage(Messages.getPREFIX() + "§7Dein Report gegen §6"
                            + reported.getName() + "§7 wird gerade von einem Teammitglied bearbeitet.");

                } else if(report.getStatus() == ReportStatus.IN_REVIEW) {
                    Player reporter = Bukkit.getPlayer(report.getReporter());
                    Player reported = Bukkit.getPlayer(report.getTarget());

                    reportObservationService.stop(player);

                    completeReport(
                            player,
                            reportDetailHolder.getReportService(),
                            report
                    );
                }


                ReportDetailGUI.open(player, reportDetailHolder.getReportService(), report.getId());

            }

            case 16 -> {
                Player reporter = Bukkit.getPlayer(report.getReporter());
                Player reported = Bukkit.getPlayer(report.getTarget());

                reportObservationService.stop(player);

                reportDetailHolder.getReportService().dismissReport(report.getId(), player.getUniqueId());

                player.sendMessage(Messages.getPREFIX() + "§7Report §6#" + report.getId() + " §7wurde §cabgelehnt§7.");

                reporter.sendMessage(Messages.getPREFIX() + "§7Dein Report gegen §6"
                        + reported.getName() + "§7 wurde von einem Teammitglied bearbeitet. Danke für deine Mithilfe!");

                ReportListGUI.open(player, reportDetailHolder.getReportService(),
                        0, reportDetailHolder.getReportFilter());
            }

            default -> {

            }

        }

    }

    private void handleReportPunishment(
            InventoryClickEvent event,
            Player player,
            ReportPunishmentHolder holder
    ) {
        int slot = event.getRawSlot();

        if(slot == 22) {
            ReportDetailGUI.open(
                    player,
                    holder.getReportService(),
                    holder.getReportId()
            );
            return;
        }

        PunishmentCategory category = switch (slot) {
            case 10 -> PunishmentCategory.MOVEMENT_HACKS;
            case 12 -> PunishmentCategory.COMBAT_HACKS;
            case 14 -> PunishmentCategory.INVENTORY_HACKS;
            case 16 -> PunishmentCategory.EXPLOITS;
            case 20 -> PunishmentCategory.OTHER;
            default -> null;
        };

        if(category == null) {
            return;
        }

        ReportPunishmentTypeGUI.open(
                player,
                holder.getReportService(),
                holder.getReportId(),
                category
        );
    }

    private void handleReportPunishmentType(
            InventoryClickEvent event,
            Player player,
            ReportPunishmentTypeHolder holder
    ) {
        int slot = event.getRawSlot();

        if(slot == 22) {
            ReportPunishmentGUI.open(
                    player,
                    holder.getReportService(),
                    holder.getReportId()
            );
            return;
        }

        switch(slot) {
            case 11 -> {
                ReportPunishmentDurationGUI.open(
                        player,
                        holder.getReportService(),
                        holder.getReportId(),
                        holder.getPunishmentCategory(),
                        PunishmentType.BAN
                );
            }

            case 13 -> {
                ReportPunishmentDurationGUI.open(
                        player,
                        holder.getReportService(),
                        holder.getReportId(),
                        holder.getPunishmentCategory(),
                        PunishmentType.MUTE
                );
            }

            case 15 -> {
                player.sendMessage(
                        Messages.getPREFIX() +
                                "§eKick §eausgewählt."
                );

            }

            default -> {
            }
        }
    }

    private void handleReportPunishmentDuration(
            InventoryClickEvent event,
            Player player,
            ReportPunishmentDurationHolder holder
    ) {
        int slot = event.getRawSlot();

        if(slot == 22) {
            ReportPunishmentTypeGUI.open(
                    player,
                    holder.getReportService(),
                    holder.getReportId(),
                    holder.getPunishmentCategory()
            );
            return;
        }

        if(slot != 13) {
            return;
        }

        String durationInput = switch(holder.getPunishmentCategory()) {
            case MOVEMENT_HACKS -> "7d";
            case COMBAT_HACKS -> "14d";
            case INVENTORY_HACKS -> "3d";
            case EXPLOITS -> "4d";
            case OTHER -> null;
        };

        if(durationInput == null) {
            player.closeInventory();

            player.sendMessage(
                    Messages.getPREFIX()
                            + "§7Gib die gewünschte Dauer ein."
            );

            return;
        }

        Report report = holder.getReportService()
                .getReport(holder.getReportId());

        if(report == null) {
            player.closeInventory();
            return;
        }

        Player target = Bukkit.getPlayer(report.getTarget());

        if(target == null) {
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return;
        }

        Duration duration;

        try {
            duration = DurationParser.parse(durationInput);
        } catch(IllegalArgumentException exception) {
            player.sendMessage(
                    Messages.getPREFIX()
                            + "§cUngültige Bestrafungsdauer."
            );
            return;
        }

        String reason = report.getReason();

        switch(holder.getPunishmentType()) {

            case BAN -> {
                punishmentService.ban(
                        target.getUniqueId(),
                        player.getUniqueId(),
                        holder.getPunishmentCategory(),
                        reason,
                        duration
                );

                target.kickPlayer(
                        "§cDu wurdest vom Server gebannt!\n\n"
                                + "§7Grund: §6" + reason + "\n"
                                + "§7Dauer: §6" + durationInput
                );

                player.sendMessage(
                        Messages.getPREFIX()
                                + "§7Spieler §6" + target.getName()
                                + "§7 wurde für §6" + durationInput
                                + " §7gebannt."
                );
            }

            case MUTE -> {
                punishmentService.mute(
                        target.getUniqueId(),
                        player.getUniqueId(),
                        holder.getPunishmentCategory(),
                        reason,
                        duration
                );

                player.sendMessage(
                        Messages.getPREFIX()
                                + "§7Spieler §6" + target.getName()
                                + "§7 wurde für §6" + durationInput
                                + " §7gemutet."
                );
            }

            case KICK -> {
                punishmentService.punish(
                        target.getUniqueId(),
                        player.getUniqueId(),
                        PunishmentType.KICK,
                        holder.getPunishmentCategory(),
                        reason,
                        Duration.ZERO
                );

                target.kickPlayer(
                        "§cDu wurdest vom Server gekickt!\n\n"
                                + "§7Grund: §6" + reason
                );

                player.sendMessage(
                        Messages.getPREFIX()
                                + "§7Spieler §6" + target.getName()
                                + "§7 wurde gekickt."
                );
            }
        }

        completeReport(
                player,
                holder.getReportService(),
                report
        );

        player.closeInventory();
    }

    private void completeReport(
            Player moderator,
            ReportService reportService,
            Report report
    ) {
        Player reporter = Bukkit.getPlayer(report.getReporter());
        Player reported = Bukkit.getPlayer(report.getTarget());

        reportObservationService.stop(moderator);

        reportService.resolveReport(
                report.getId(),
                moderator.getUniqueId()
        );

        moderator.sendMessage(
                Messages.getPREFIX()
                        + "§7Report §6#" + report.getId()
                        + "§7 wurde §aabgeschlossen§f."
        );

        if(reporter != null) {
            reporter.sendMessage(
                    Messages.getPREFIX()
                            + "§7Dein Report gegen §6"
                            + (reported != null
                            ? reported.getName()
                            : "den Spieler")
                            + "§7 wurde von einem Teammitglied bearbeitet. "
                            + "Danke für deine Mithilfe!"
            );
        }

        moderator.playSound(
                moderator,
                Sound.ENTITY_PLAYER_LEVELUP,
                1,
                2
        );
    }

    private void executePunishment(
            Player moderator,
            Report report,
            PunishmentType punishmentType,
            PunishmentCategory punishmentCategory,
            String durationInput
    ) {
        Player target = Bukkit.getPlayer(report.getTarget());

        if(target == null) {
            moderator.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return;
        }

        Duration duration;

        try {
            duration = DurationParser.parse(durationInput);
        } catch(IllegalArgumentException exception) {
            moderator.sendMessage(
                    Messages.getPREFIX()
                            + "§cUngültige Bestrafungsdauer."
            );
            return;
        }

        String reason = report.getReason();

        switch(punishmentType) {

            case BAN -> {
                punishmentService.ban(
                        target.getUniqueId(),
                        moderator.getUniqueId(),
                        punishmentCategory,
                        reason,
                        duration
                );

                target.kickPlayer(
                        "§cDu wurdest vom Server gebannt!\n\n"
                                + "§7Grund: §6" + reason + "\n"
                                + "§7Dauer: §6" + durationInput
                );
            }

            case MUTE -> {
                punishmentService.mute(
                        target.getUniqueId(),
                        moderator.getUniqueId(),
                        punishmentCategory,
                        reason,
                        duration
                );

                moderator.sendMessage(
                        Messages.getPREFIX()
                                + "§7Spieler §6" + target.getName()
                                + "§7 wurde für §6" + durationInput
                                + " §7gemutet."
                );
            }

            case KICK -> {
                punishmentService.punish(
                        target.getUniqueId(),
                        moderator.getUniqueId(),
                        PunishmentType.KICK,
                        punishmentCategory,
                        reason,
                        Duration.ZERO
                );

                target.kickPlayer(
                        "§cDu wurdest vom Server gekickt!\n\n"
                                + "§7Grund: §6" + reason
                );
            }
        }

        completeReport(
                moderator,
                report,
                report.getId()
        );
    }

}
