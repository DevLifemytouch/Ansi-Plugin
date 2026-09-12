package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import de.lifemytouch.ansi.punish.PunishmentCategory;
import de.lifemytouch.ansi.punish.PunishmentType;
import de.lifemytouch.ansi.report.Report;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ReportPunishmentDurationGUI {

    public static final String TITLE = "§c§lBestrafungsdauer";

    private ReportPunishmentDurationGUI() {
    }

    public static void open(
            Player player,
            ReportService reportService,
            long reportId,
            PunishmentCategory category,
            PunishmentType punishmentType
    ) {
        Report report = reportService.getReport(reportId);

        if (report == null) {
            player.closeInventory();
            return;
        }

        String duration = switch (category) {
            case MOVEMENT_HACKS -> "7d";
            case COMBAT_HACKS -> "14d";
            case INVENTORY_HACKS -> "3d";
            case EXPLOITS -> "4d";
            case OTHER -> null;
        };

        ReportPunishmentDurationHolder holder =
                new ReportPunishmentDurationHolder(
                        reportId,
                        reportService,
                        category,
                        punishmentType,
                        duration
                );

        Inventory inventory = Bukkit.createInventory(
                holder,
                27,
                TITLE + " §8#" + reportId
        );

        inventory.setItem(
                4,
                ItemBuilder.createItem(
                        Material.BOOK,
                        "§6§lBestrafung",
                        List.of(
                                "§7Spieler: §6" + getPlayerName(report),
                                "§7Kategorie: §6"
                                        + getCategoryName(category),
                                "§7Typ: §6"
                                        + getPunishmentName(punishmentType)
                        )
                )
        );

        switch (category) {

            case MOVEMENT_HACKS -> setDuration(
                    inventory,
                    "7 Tage",
                    "7d"
            );

            case COMBAT_HACKS -> setDuration(
                    inventory,
                    "14 Tage",
                    "14d"
            );

            case INVENTORY_HACKS -> setDuration(
                    inventory,
                    "3 Tage",
                    "3d"
            );

            case EXPLOITS -> setDuration(
                    inventory,
                    "4 Tage",
                    "4d"
            );

            case OTHER -> inventory.setItem(
                    13,
                    ItemBuilder.createItem(
                            Material.NAME_TAG,
                            "§e§lCustom",
                            List.of(
                                    "§7Eigene Dauer",
                                    "",
                                    "§7Klicke, um eine eigene",
                                    "§7Dauer einzugeben."
                            )
                    )
            );
        }

        inventory.setItem(
                22,
                ItemBuilder.createItem(
                        Material.BARRIER,
                        "§7§lZurück",
                        List.of(
                                "§7Zurück zur Auswahl."
                        )
                )
        );

        player.openInventory(inventory);
    }

    private static void setDuration(
            Inventory inventory,
            String display,
            String duration
    ) {
        inventory.setItem(
                13,
                ItemBuilder.createItem(
                        Material.CLOCK,
                        "§e§l" + display,
                        List.of(
                                "§7Dauer: §6" + duration,
                                "",
                                "§aKlicke zum Auswählen."
                        )
                )
        );
    }

    private static String getPlayerName(Report report) {
        String name = Bukkit.getOfflinePlayer(
                report.getTarget()
        ).getName();

        return name != null
                ? name
                : report.getTarget().toString().substring(0, 8);
    }

    private static String getCategoryName(
            PunishmentCategory category
    ) {
        return switch (category) {
            case MOVEMENT_HACKS -> "Movement Hacks";
            case COMBAT_HACKS -> "Combat Hacks";
            case INVENTORY_HACKS -> "Inventory Hacks";
            case EXPLOITS -> "Exploits";
            case OTHER -> "Other";
        };
    }

    private static String getPunishmentName(
            PunishmentType type
    ) {
        return switch (type) {
            case BAN -> "Ban";
            case MUTE -> "Mute";
            case KICK -> "Kick";
        };
    }
}