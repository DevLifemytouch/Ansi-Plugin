package de.lifemytouch.ansi.challenge.setting;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ChallengeSettingGUI {

    public static final String TITLE = "§x§B§0§9§5§2§1§lC§x§B§3§9§8§2§4§lh§x§B§5§9§B§2§8§la§x§B§8§9§E§2§B§ll§x§B§A§A§" +
            "1§2§E§ll§x§B§D§A§4§3§1§le§x§C§0§A§7§3§5§ln§x§C§2§A§A§3§8§lg§x§C§5§A§D§3§B§le §x§C§A§B§3§4§2§lE§x§C§D§B§6" +
            "§4§5§li§x§C§F§B§8§4§8§ln§x§D§2§B§B§4§C§ls§x§D§4§B§E§4§F§lt§x§D§7§C§1§5§2§le§x§D§9§C§4§5§5§ll§x§D§C§C§7§5" +
            "§9§ll§x§D§F§C§A§5§C§lu§x§E§1§C§D§5§F§ln§x§E§4§D§0§6§2§lg§x§E§6§D§3§6§6§le§x§E§9§D§6§6§9§ln";
    public static final String HARDCOREITEM = "§x§F§F§0§0§0§0§lH§x§F§F§0§C§0§C§la§x§F§F§1§9§1§9§lr§x§F§F§2§5§2§5§ld§x" +
            "§F§F§3§1§3§1§lc§x§F§F§3§D§3§D§lo§x§F§F§4§A§4§A§lr§x§F§F§5§6§5§6§le";
    public static final String BLOCKRANDOMIZERITEM = "§x§0§0§F§F§A§2§lB§x§0§6§F§F§A§6§ll§x§0§B§F§F§A§A§lo§x§1§1§F§F§A" +
            "§E§lc§x§1§7§F§F§B§2§lk §x§2§2§F§F§B§A§lR§x§2§8§F§F§B§E§la§x§2§E§F§F§C§1§ln§x§3§4§F§F§C§5§ld§x§3§9§F§F§C§9" +
            "§lo§x§3§F§F§F§C§D§lm§x§4§5§F§F§D§1§li§x§4§B§F§F§D§5§lz§x§5§0§F§F§D§9§le§x§5§6§F§F§D§D§lr";

    private static ChallengeSettingManager challengeSettingManager;

    public static void init(ChallengeSettingManager manager) {
        challengeSettingManager = manager;
    }

    public static void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, TITLE);

        ItemStack hardcoreItem = ItemBuilder.createItem(challengeSettingManager.isHardcore()
                        ? Material.TOTEM_OF_UNDYING
                        : Material.SKELETON_SKULL, HARDCOREITEM, Arrays.asList(
                                "",
                "§7Status: " + (challengeSettingManager.isHardcore()
                        ? "§aAktiviert"
                        : "§cDeaktiviert"),
                "",
                "§7Klicke um Hardcore",
                "§7 " + (challengeSettingManager.isHardcore()
                        ? "zu §cdeaktivieren."
                        : "zu §aaktivieren.")
        ));

        inventory.setItem(0, hardcoreItem);

        ItemStack blockRandomizerItem = ItemBuilder.createItem(challengeSettingManager.isHardcore()
                ? Material.ENDER_EYE
                : Material.ENDER_PEARL, BLOCKRANDOMIZERITEM, Arrays.asList(
                "",
                "§7Status: " + (challengeSettingManager.isBlockRandomizer()
                        ? "§aAktiviert"
                        : "§cDeaktiviert"),
                "",
                "§7Klicke um den",
                "§7Block Randomizer",
                "§7 " + (challengeSettingManager.isBlockRandomizer()
                        ? "zu §cdeaktivieren."
                        : "zu §aaktivieren.")
        ));

        inventory.setItem(1, blockRandomizerItem);

        player.openInventory(inventory);
    }
}
