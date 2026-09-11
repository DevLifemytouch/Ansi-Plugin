package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import de.lifemytouch.ansi.report.Report;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReportListGUI {

    public static final String TITLE = "§x§3§C§3§C§3§C§lO§x§3§F§3§F§3§F§lf§x§4§2§4§2§4§2§lf§x§4§5§4§5§4§5§" +
            "le§x§4§8§4§8§4§8§ln§x§4§B§4§B§4§B§le §x§5§0§5§0§5§0§lR§x§5§3§5§3§5§3§le§x§5§6§5§6§5§6§lp§x§5§" +
            "9§5§9§5§9§lo§x§5§C§5§C§5§C§lr§x§5§F§5§F§5§F§lt§x§6§2§6§2§6§2§ls";

    private static final int PAGE_SIZE = 45;

    private ReportListGUI() {

    }

    public static void open(Player player, ReportService reportService, int page) {
        List<Report> reports = new ArrayList<>();

        reports.addAll(reportService.getOpenReports());
        reports.addAll(reportService.getReportsInReview());

        reports.sort((first, second) -> Long.compare(second.getId(), first.getId()));

        int totalPages = Math.max(1, (int) Math.ceil(reports.size() / (double) PAGE_SIZE));

        page = Math.max(1, (int) Math.ceil(reports.size() / (double) PAGE_SIZE));

        ReportListHolder reportListHolder = new ReportListHolder(reportService, page);

        Inventory inventory = Bukkit.createInventory(reportListHolder, 54, TITLE + " §8(" + (page + 1) + "/" + totalPages + ")");

        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, reports.size());

        for(int index = start; index < end; index++) {
            Report report = reports.get(index);

            int slot = index - start;

            inventory.setItem(slot, createReportItem(report));
        }

        if(page > 0) {
            inventory.setItem(45, ItemBuilder.createItem(Material.ARROW, "§e§lVorherige Seite", List.of("")));
        }

        inventory.setItem(49, ItemBuilder.createItem(Material.BARRIER, "§c§lSchließen", List.of("")));

        if(page < totalPages - 1) {
            inventory.setItem(53, ItemBuilder.createItem(Material.ARROW, "§e§lNächste Seite", List.of("")));
        }

        player.openInventory(inventory);

    }

    private static ItemStack createReportItem(Report report) {
        Material material;

        if (report.getCategory().name().equals("HACKING")) {
            material = Material.IRON_SWORD;
        } else {
            material = Material.PAPER;
        }

        String status = switch (report.getStatus()) {
            case PENDING -> "§eOffen";
            case IN_REVIEW -> "§6In Bearbeitung";
            case RESOLVED -> "§aErledigt";
            case DISMISSED -> "§cAbgelehnt";
        };

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        return ItemBuilder.createItem(material, "§c§lReport §8#" + report.getId(),
                Arrays.asList(
                        "§7Spieler: §8" + getPlayerName(report.getTarget()),
                        "§7Reporter: §8" + getPlayerName(report.getReporter()),
                        "§7Kategorie: §8" + report.getCategory().name(),
                        "§7Grund: §8" + report.getReason(),
                        "§7Status: " + status,
                        "§7Erstellt: §8" + simpleDateFormat.format(new Date(report.getCreatedAt()))
                )
        );
    }

    private static String getPlayerName(UUID uuid) {
        String name = Bukkit.getOfflinePlayer(uuid).getName();

        return name != null ? name : uuid.toString().substring(0, 8);
    }

}
