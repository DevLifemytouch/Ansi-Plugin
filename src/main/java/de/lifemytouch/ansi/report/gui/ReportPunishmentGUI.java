package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import de.lifemytouch.ansi.report.Report;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class ReportPunishmentGUI {

    public static final String TITLE = "§c§lPunishment";

    private ReportPunishmentGUI() {

    }

    public static void open(Player player, ReportService reportService, long reportId) {
        Report report = reportService.getReport(reportId);

        if(report == null) {
            player.closeInventory();
            return;
        }

        ReportPunishmentHolder reportPunishmentHolder = new ReportPunishmentHolder(reportId, reportService);

        Inventory inventory = Bukkit.createInventory(reportPunishmentHolder, 27, TITLE + "§8#" + reportId);

        inventory.setItem(4, ItemBuilder.createItem(
                Material.BOOK,
                "§6§lBestrafung auswählen",
                Arrays.asList(
                        "§7Spieler: §6" + getPlayerName(report),
                        "§7Report-Kategorie: §6" + report.getCategory().name()
                )
        ));

        if(report.getCategory().name().equals("HACKING")) {

            inventory.setItem(
                    10,
                    ItemBuilder.createItem(
                            Material.FEATHER,
                            "§e§lMovement Hacks",
                            Arrays.asList(
                                    "§7Speed, Fly, NoFall",
                                    "§7und ähnliche Hacks."
                            )
                    )
            );

            inventory.setItem(
                    12,
                    ItemBuilder.createItem(
                            Material.DIAMOND_SWORD,
                            "§c§lCombat Hacks",
                            Arrays.asList(
                                    "§7KillAura, Reach, Aimbot",
                                    "§7und ähnliche Hacks."
                            )
                    )
            );

            inventory.setItem(
                    14,
                    ItemBuilder.createItem(
                            Material.CHEST,
                            "§6§lInventory Hacks",
                            Arrays.asList(
                                    "§7ChestStealer, AutoArmor",
                                    "§7und ähnliche Hacks."
                            )
                    )
            );

            inventory.setItem(
                    16,
                    ItemBuilder.createItem(
                            Material.TNT,
                            "§4§lExploits",
                            Arrays.asList(
                                    "§7Ausnutzen von Exploits",
                                    "§7oder Spielfehlern."
                            )
                    )
            );

            inventory.setItem(
                    22,
                    ItemBuilder.createItem(
                            Material.PAPER,
                            "§7§lOther",
                            Arrays.asList(
                                    "§7Sonstige Hacking-Verstöße."
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
                                "§7Zurück zum Report."
                        )
                )
        );

        player.openInventory(inventory);

    }

    private static String getPlayerName(Report report) {
        String name =
                Bukkit.getOfflinePlayer(
                        report.getTarget()
                ).getName();

        return name != null
                ? name
                : report.getTarget()
                  .toString()
                  .substring(0, 8);
    }

}
