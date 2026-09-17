package de.lifemytouch.ansi.world.inventories;

import de.lifemytouch.ansi.coin.CoinService;
import de.lifemytouch.ansi.cosmetic.Cosmetic;
import de.lifemytouch.ansi.cosmetic.CosmeticRegistry;
import de.lifemytouch.ansi.core.item.ItemBuilder;
import de.lifemytouch.ansi.cosmetic.CosmeticService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CosmeticsShopInventory {

    private static final String TITLE = "§6§lShop";

    public static void open(
            Player player,
            CosmeticRegistry cosmeticRegistry,
            CosmeticService cosmeticService
    ) {

        CosmeticsShopInventoryHolder holder = new CosmeticsShopInventoryHolder();

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
                "§6§lCosmetics",
                List.of(
                        "§7Öffnet dein Cosmetics-",
                        "§7Inventar"
                )
        ));

        inventory.setItem(49, ItemBuilder.createItem(
                Material.HOPPER,
                "§7§lFilter",
                List.of()
        ));

        inventory.setItem(53, ItemBuilder.createItem(
                Material.ARROW,
                "§eNächste Seite",
                List.of()
        ));

        int slot = 10;

        for(Cosmetic cosmetic : cosmeticRegistry.getAll()) {

            if(slot >= 45) {
                break;
            }

            ItemStack item = cosmetic.getItem().clone();

            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();

                if(!cosmeticService.hasCosmetic(player.getUniqueId(), cosmetic.getId())) {
                    meta.setLore(List.of(
                            "§7Preis: §e" + cosmetic.getPrice() + " Coins",
                            "",
                            "§aKlicke zum Kaufen"
                    ));
                } else {
                    meta.setLore(List.of(
                            "§cDu besitzt dieses Item",
                            "§cbereits."
                    ));
                }

                item.setItemMeta(meta);
            }

            inventory.setItem(slot, item);

            slot++;
        }

        player.openInventory(inventory);
    }
}