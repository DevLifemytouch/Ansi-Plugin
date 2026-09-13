package de.lifemytouch.ansi.world.inventories;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class CosmeticsInventory {
    private static final String TITLE = "§6§lCosmetics";

    public static void open(Player player) {

        CosmeticsInventoryHolder holder = new CosmeticsInventoryHolder();

        Inventory inventory = Bukkit.createInventory(holder, 54, TITLE);

        for(int i = 0; i < 9; i++) {
            inventory.setItem(i, ItemBuilder.createItem(
                    Material.LIME_STAINED_GLASS_PANE,
                    " ",
                    List.of()
            ));
        }

        inventory.setItem(9, ItemBuilder.createItem(Material.LIME_STAINED_GLASS_PANE, " ", List.of()));
        inventory.setItem(17, ItemBuilder.createItem(Material.LIME_STAINED_GLASS_PANE, " ", List.of()));
        inventory.setItem(18, ItemBuilder.createItem(Material.LIME_STAINED_GLASS_PANE, " ", List.of()));
        inventory.setItem(26, ItemBuilder.createItem(Material.LIME_STAINED_GLASS_PANE, " ", List.of()));
        inventory.setItem(27, ItemBuilder.createItem(Material.LIME_STAINED_GLASS_PANE, " ", List.of()));
        inventory.setItem(35, ItemBuilder.createItem(Material.LIME_STAINED_GLASS_PANE, " ", List.of()));

        for(int i = 36; i < 45; i++) {
            inventory.setItem(i, ItemBuilder.createItem(
                    Material.LIME_STAINED_GLASS_PANE,
                    " ",
                    List.of()
            ));
        }

        inventory.setItem(45, ItemBuilder.createCustomHead(
                "cc1b2f592cfc8d372dcf5fd44eed69dddc64601d7846d72619f70511d8043a89",
                "§6§lShop",
                List.of("§7Öffnet den Shop \n" +
                        "für Cosmetics")
        ));

        inventory.setItem(49, ItemBuilder.createItem(Material.HOPPER, "§7§lFilter", List.of()));
        inventory.setItem(53, ItemBuilder.createItem(Material.ARROW, "§eNächste Seite", List.of()));

        player.openInventory(inventory);

    }
}
