package de.lifemytouch.ansi.world.inventories;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class NavInventory {

    private static final String TITLE = "§c§lNavigator";

    public static void open(Player player) {

        NavInventoryHolder holder = new NavInventoryHolder();

        Inventory inventory = Bukkit.createInventory(holder, 27, TITLE);

        // 10, 13, 16

        inventory.setItem(10,
                ItemBuilder.createItem(
                        Material.MACE,
                        "§8§kX§c§lPVP§8§kX",
                        List.of(
                                "§7Teleportiert dich zum\n",
                                "§7PVP-NPC."
                        )
                )
        );

        inventory.setItem(13,
                ItemBuilder.createItem(
                        Material.BEACON,
                        "§8§kX§b§lSpawn§8§kX",
                        List.of(
                                "§7Teleportiert dich zum\n",
                                "§7Spawn."
                        )
                )
        );

        inventory.setItem(16,
                ItemBuilder.createItem(
                        Material.GRASS_BLOCK,
                        "§8§kX§2§lSMP§8§kX",
                        List.of(
                                "§7Teleportiert dich zum\n",
                                "§7SMP-NPC."
                        )
                )
        );

        player.openInventory(inventory);

    }

}
