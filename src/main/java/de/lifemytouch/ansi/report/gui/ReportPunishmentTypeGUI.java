package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import de.lifemytouch.ansi.punish.PunishmentCategory;
import de.lifemytouch.ansi.report.Report;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ReportPunishmentTypeGUI {

    public static final String TITLE = "§c§lPunishment";

    private ReportPunishmentTypeGUI() {
    }

    public static void open(
            Player player,
            ReportService reportService,
            long reportId,
            PunishmentCategory punishmentCategory
    ) {
        Report report = reportService.getReport(reportId);

        if (report == null) {
            player.closeInventory();
            return;
        }

        ReportPunishmentTypeHolder holder =
                new ReportPunishmentTypeHolder(
                        reportId,
                        reportService,
                        punishmentCategory
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
                        "§6§lBestrafung auswählen",
                        List.of(
                                "§7Spieler: §6" + getPlayerName(report),
                                "§7Kategorie: §6"
                                        + getCategoryName(punishmentCategory)
                        )
                )
        );

        inventory.setItem(
                11,
                ItemBuilder.createItem(
                        Material.ANVIL,
                        "§4§lBan",
                        List.of(
                                "§7Spieler dauerhaft oder",
                                "§7temporär §4bannen."
                        )
                )
        );

        inventory.setItem(
                13,
                ItemBuilder.createItem(
                        Material.PAPER,
                        "§c§lMute",
                        List.of(
                                "§7Spieler dauerhaft oder",
                                "§7temporär §cmuten."
                        )
                )
        );

        inventory.setItem(
                15,
                ItemBuilder.createItem(
                        Material.IRON_BOOTS,
                        "§e§lKick",
                        List.of(
                                "§7Spieler vom Server",
                                "§ekicken."
                        )
                )
        );

        inventory.setItem(
                22,
                ItemBuilder.createItem(
                        Material.BARRIER,
                        "§7§lZurück",
                        List.of(
                                "§7Zurück zur Kategorie-Auswahl."
                        )
                )
        );

        player.openInventory(inventory);
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
            PunishmentCategory punishmentCategory
    ) {
        return switch (punishmentCategory) {
            case MOVEMENT_HACKS -> "Movement Hacks";
            case COMBAT_HACKS -> "Combat Hacks";
            case INVENTORY_HACKS -> "Inventory Hacks";
            case EXPLOITS -> "Exploits";
            case OTHER -> "Other";
        };
    }
}