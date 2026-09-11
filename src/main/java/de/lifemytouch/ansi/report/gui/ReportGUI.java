package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class ReportGUI {

    private static final String TITLE = "§x§8§F§8§F§8§F§lS§x§9§2§9§2§9§2§lp§x§9§6§9§6§9§6§li§" +
            "x§9§9§9§9§9§9§le§x§9§D§9§D§9§D§ll§x§A§0§A§0§A§0§le§x§A§4§A§4§A§4§lr §x§A§B§A§B§A" +
            "§B§lm§x§A§E§A§E§A§E§le§x§B§2§B§2§B§2§ll§x§B§5§B§5§B§5§ld§x§B§9§B§9§B§9§le§x§B§C§" +
            "B§C§B§C§ln";

    private static final String CHEATING_ITEM = "§c§lHacking §7/ §cCheating";
    private static final String CHAT_ITEM = "§e§lChat";
    private static final String BUGUSING_ITEM = "§6§lBugusing";
    private static final String ADVERTISING_ITEM = "§b§lWerbung";
    private static final String OTHER_ITEM = "§7§lOther";

    private ReportGUI() {

    }

    public static void open(
            Player reporter,
            Player target,
            ReportService reportService
    ) {
        Inventory inventory = Bukkit.createInventory(null, 27, TITLE);

        inventory.setItem(10, ItemBuilder.createItem(
                Material.IRON_SWORD,
                CHEATING_ITEM,
                Arrays.asList(
                        "§7Melde einen Spieler wegen\n",
                        "§7unerlaubten Mods oder Cheats."
                )
                )
        );

        inventory.setItem(12, ItemBuilder.createItem(
                        Material.PAPER,
                        CHAT_ITEM,
                        Arrays.asList(
                                "§7Beleidigungen, Spam,\n",
                                "§7Hassrede usw."
                        )
                )
        );

        inventory.setItem(14, ItemBuilder.createItem(
                        Material.BARRIER,
                        BUGUSING_ITEM,
                        Arrays.asList(
                                "§7Melde einen Spieler für\n",
                                "§7das Ausnutzen von Bugs."
                        )
                )
        );

        inventory.setItem(16, ItemBuilder.createItem(
                        Material.OAK_SIGN,
                        ADVERTISING_ITEM,
                        Arrays.asList(
                                "§7Unerlaubte Werbung"
                        )
                )
        );

        inventory.setItem(22, ItemBuilder.createItem(
                        Material.BOOK,
                        OTHER_ITEM,
                        Arrays.asList(
                                "§7Melde einen Spieler für\n",
                                "§7andere Verstöße gegen die Serverregeln."
                        )
                )
        );

        reporter.openInventory(inventory);

    }
}
