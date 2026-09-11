package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import de.lifemytouch.ansi.report.Report;
import de.lifemytouch.ansi.report.ReportService;
import de.lifemytouch.ansi.report.ReportStatus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class ReportDetailGUI {

    public static final String TITLE = "§x§8§F§8§F§8§F§lR§x§9§3§9§3§9§3§le§x§9§7§9§7§9§7§lp§x§9§A§9§A§9§A§lo§x§9§E" +
            "§9§E§9§E§lr§x§A§2§A§2§A§2§lt §x§A§9§A§9§A§9§lD§x§A§D§A§D§A§D§le§x§B§1§B§1§B§1§lt§x§B§5§B§5§B§5§la§x§B" +
            "§8§B§8§B§8§li§x§B§C§B§C§B§C§ll";

    private ReportDetailGUI() {
    }

    public static void open(Player player, ReportService reportService, long reportId) {
        Report report = reportService.getReport(reportId);

        if(report == null) {
            player.closeInventory();
            return;
        }

        ReportDetailHolder reportDetailHolder = new ReportDetailHolder(reportId, reportService);

        Inventory inventory = Bukkit.createInventory(reportDetailHolder, 27, TITLE + " §8#" + reportId);

        inventory.setItem(4, createReportInfo(report));

        inventory.setItem(
                11,
                ItemBuilder.createItem(
                        Material.ENDER_EYE,
                        "§b§lBeobachten",
                        Arrays.asList("§cWIP")
                )
        );

        inventory.setItem(
                13,
                ItemBuilder.createItem(
                        Material.COMPASS,
                        "§e§lZum Spieler",
                        Arrays.asList(
                                "§7Teleportiert dich zum\n",
                                "§7gemeldeten Spieler"
                        )
                )
        );

        if(report.getStatus() == ReportStatus.PENDING) {
            inventory.setItem(
                    15,
                    ItemBuilder.createItem(
                            Material.LIME_DYE,
                            "§a§lReport bearbeiten",
                            Arrays.asList(
                                    "§7Übernimmt den Report\n",
                                    "§7und setzt ihn auf\n",
                                    "§6§lIn Bearbeitung§7!"
                            )
                    )
            );

            inventory.setItem(
                    16,
                    ItemBuilder.createItem(
                            Material.RED_DYE,
                            "§c§lReport ablehnen",
                            Arrays.asList(
                                    "§7Der Report wird als\n",
                                    "§c§labgelehnt §7markiert."
                            )
                    )
            );
        } else if (report.getStatus() == ReportStatus.IN_REVIEW) {
            inventory.setItem(
                    15,
                    ItemBuilder.createItem(
                            Material.LIME_DYE,
                            "§a§lReport abschließen",
                            Arrays.asList(
                                    "§7Der Report wird\n",
                                    "§aerledigt §7markiert."
                            )
                    )
            );

            inventory.setItem(
                    16,
                    ItemBuilder.createItem(
                            Material.RED_DYE,
                            "§c§lReport ablehnen",
                            Arrays.asList(
                                    "§7Der Report wird als\n",
                                    "§c§labgelehnt §7markiert."
                            )
                    )
            );
        }




        inventory.setItem(
                22,
                ItemBuilder.createItem(
                        Material.BARRIER,
                        "§7§lZurück",
                        Arrays.asList(
                                "§7Zurück zur Report-Liste."
                        )
                )
        );

        player.openInventory(inventory);

    }

    private static ItemStack createReportInfo(Report report) {
        String status = switch (report.getStatus()) {
            case PENDING -> "§eOffen";
            case IN_REVIEW -> "§6In Bearbeitung";
            case RESOLVED -> "§aErledigt";
            case DISMISSED -> "§cAbgelehnt";
        };

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        return ItemBuilder.createItem(Material.BOOK,
                "§6§lReport §8#" + report.getId(),
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
