package de.lifemytouch.ansi.challenge.gui;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class ChallengeGUI {
    public static final String TITLE = "§x§B§0§9§5§2§1§lC§x§B§4§9§A§2§7§lh§x§B§9§9§F§2§C§la§x§B§D§A§4§3§2§ll§x§C" +
            "§2§A§9§3§7§ll§x§C§6§A§E§3§D§le§x§C§A§B§3§4§2§ln§x§C§F§B§8§4§8§lg§x§D§3§B§D§4§D§le §x§D§C§C§7§5§8§lM" +
            "§x§E§0§C§C§5§E§le§x§E§5§D§1§6§3§ln§x§E§9§D§6§6§9§lü";
    public static final String allItemsName = "§x§2§F§2§1§B§0§lA§x§3§6§2§B§B§7§ll§x§3§E§3§5§B§E§ll §x§4§C§4§A§C§" +
            "D§lI§x§5§3§5§4§D§4§lt§x§5§B§5§E§D§B§le§x§6§2§6§8§E§2§lm§x§6§9§7§2§E§9§ls";
    public static final String allMobsName = "§x§A§C§5§B§7§F§lA§x§A§8§5§D§7§D§ll§x§A§4§5§F§7§C§ll §x§9§D§6§2§7§9" +
            "§lM§x§9§9§6§4§7§7§lo§x§9§5§6§6§7§6§lb§x§9§1§6§8§7§4§ls";

    public static Inventory build() {
        Inventory inventory = Bukkit.createInventory(null, 27, TITLE);

        inventory.setItem(0, ItemBuilder.createItem(Material.DIAMOND, allItemsName, Arrays.asList("")));
        inventory.setItem(1, ItemBuilder.createItem(Material.WARDEN_SPAWN_EGG, allMobsName, Arrays.asList("")));
        return inventory;
    }

    public static void open(Player player) {
        player.openInventory(build());
    }
}
