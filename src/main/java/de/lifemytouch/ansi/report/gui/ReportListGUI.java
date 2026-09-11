package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import de.lifemytouch.ansi.report.Report;
import de.lifemytouch.ansi.report.ReportFilter;
import de.lifemytouch.ansi.report.ReportService;
import de.lifemytouch.ansi.report.ReportStatus;
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

    public static void open(
            Player player,
            ReportService reportService,
            int page,
            ReportFilter filter
    ) {
        List<Report> reports = getFilteredReports(reportService, filter);

        int totalPages = Math.max(
                1,
                (int) Math.ceil(reports.size() / (double) PAGE_SIZE)
        );

        page = Math.clamp(page, 0, totalPages - 1);

        ReportListHolder holder = new ReportListHolder(
                reportService,
                page,
                filter
        );

        Inventory inventory = Bukkit.createInventory(
                holder,
                54,
                TITLE + " §8(" + (page + 1) + "/" + totalPages + ") §7- " + getFilterName(filter)
        );

        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, reports.size());

        for (int index = start; index < end; index++) {
            Report report = reports.get(index);
            int slot = index - start;

            inventory.setItem(slot, createReportItem(report));
        }

        if (page > 0) {
            inventory.setItem(
                    45,
                    ItemBuilder.createItem(
                            Material.ARROW,
                            "§e§lVorherige Seite",
                            List.of("")
                    )
            );
        }

        inventory.setItem(
                46,
                ItemBuilder.createItem(
                        Material.HOPPER,
                        "§6§lFilter",
                        List.of(
                                "§7Aktuell: §e" + getFilterName(filter),
                                "",
                                "§eKlicke, um den Filter zu wechseln."
                        )
                )
        );

        inventory.setItem(
                49,
                ItemBuilder.createItem(
                        Material.BARRIER,
                        "§c§lSchließen",
                        List.of("")
                )
        );

        if (page < totalPages - 1) {
            inventory.setItem(
                    53,
                    ItemBuilder.createItem(
                            Material.ARROW,
                            "§e§lNächste Seite",
                            List.of("")
                    )
            );
        }
        player.openInventory(inventory);
    }

    private static List<Report> getFilteredReports(ReportService reportService, ReportFilter reportFilter) {
        List<Report> reports = new ArrayList<>();

        switch (reportFilter) {
            case OPEN -> {
                reports.addAll(reportService.getOpenReports());
                reports.addAll(reportService.getReportsInReview());
            }

            case PENDING -> {
                reports.addAll(reportService.getOpenReports());
            }

            case IN_REVIEW -> {
                reports.addAll(reportService.getReportsInReview());
            }

            case RESOLVED -> {
                reports.addAll(reportService.getReportsByStatus(ReportStatus.RESOLVED));
            }

            case DISMISSED -> {
                reports.addAll(reportService.getReportsByStatus(ReportStatus.DISMISSED));
            }

            case ALL -> {
                reports.addAll(reportService.getAllReports());
            }

            case null, default -> {

            }
        }

        reports.sort(
                (first, second) ->
                        Long.compare(second.getId(), first.getId())
        );

        return reports;

    }

    public static List<Report> getReports(ReportService reportService, ReportFilter reportFilter) {
        return getFilteredReports(reportService, reportFilter);
    }

    private static String getFilterName(ReportFilter reportFilter) {
        return switch (reportFilter) {
            case OPEN -> "Offen";
            case PENDING -> "Pending";
            case IN_REVIEW -> "In Bearbeitung";
            case RESOLVED -> "Angenommen";
            case DISMISSED -> "Abgelehnt";
            case ALL -> "Alle";
        };
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
