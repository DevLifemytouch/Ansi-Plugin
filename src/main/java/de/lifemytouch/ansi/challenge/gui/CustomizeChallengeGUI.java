package de.lifemytouch.ansi.challenge.gui;

import de.lifemytouch.ansi.challenge.ChallengeType;
import de.lifemytouch.ansi.core.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class CustomizeChallengeGUI {

    public static final String TITLE = "§x§B§0§9§5§2§1§lE§x§B§5§9§A§2§7§li§x§B§A§A§0§2§D§ln§x§B§E§A§5§3§3§ls§x§C§3§A" +
            "§B§3§9§lt§x§C§8§B§0§3§F§le§x§C§D§B§6§4§5§ll§x§D§1§B§B§4§B§ll§x§D§6§C§0§5§1§lu§x§D§B§C§6§5§7§ln§x§E§0§C§B" +
            "§5§D§lg§x§E§4§D§1§6§3§le§x§E§9§D§6§6§9§ln";

    private static final String startItemTitle = "§x§3§3§B§A§2§B§lC§x§3§1§B§E§2§8§lh§x§2§F§C§3§2§6§la§x§2§C§C§7§2§3§l" +
            "l§x§2§A§C§B§2§0§ll§x§2§8§D§0§1§E§le§x§2§6§D§4§1§B§ln§x§2§3§D§8§1§8§lg§x§2§1§D§D§1§6§le §x§1§D§E§5§1§0§ls" +
            "§x§1§A§E§9§0§D§lt§x§1§8§E§E§0§B§la§x§1§6§F§2§0§8§lr§x§1§4§F§6§0§5§lt§x§1§1§F§B§0§3§le§x§0§F§F§F§0§0§ln";

    private static final String backItemTitle = "§x§B§A§2§B§2§B§lZ§x§C§8§2§2§2§2§lu§x§D§6§1§A§1§A§lr§x§E§3§1§1§1§1§lü" +
            "§x§F§1§0§9§0§9§lc§x§F§F§0§0§0§0§lk";

    private static final String settingsItemTitle = "§x§4§1§4§1§4§1§lE§x§4§6§4§6§4§6§li§x§4§B§4§B§4§B§ln§x§5§0§5§0§5§" +
            "0§ls§x§5§5§5§5§5§5§lt§x§5§A§5§A§5§A§le§x§6§0§6§0§6§0§ll§x§6§5§6§5§6§5§ll§x§6§A§6§A§6§A§lu§x§6§F§6§F§6§F§" +
            "ln§x§7§4§7§4§7§4§lg§x§7§9§7§9§7§9§le§x§7§E§7§E§7§E§ln";

    public static Inventory build(ChallengeType challengeType) {
        CustomizeChallengeHolder customizeChallengeHolder = new CustomizeChallengeHolder(challengeType);
        Inventory inventory = Bukkit.createInventory(customizeChallengeHolder, 9, TITLE);
        customizeChallengeHolder.setInventory(inventory);

        inventory.setItem(0, ItemBuilder.createItem(Material.RED_DYE, backItemTitle, Arrays.asList("")));
        inventory.setItem(4, ItemBuilder.createItem(Material.GREEN_DYE, startItemTitle, Arrays.asList("")));
        inventory.setItem(8, ItemBuilder.createItem(Material.CHEST, settingsItemTitle, Arrays.asList("")));

        return inventory;
    }

    public static void open(Player player, ChallengeType challengeType) {
        player.openInventory(build(challengeType));
    }
}
