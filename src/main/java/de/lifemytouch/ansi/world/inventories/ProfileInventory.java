package de.lifemytouch.ansi.world.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ProfileInventory {

    private static final String TITLE = "§b§lProfil";

    public static void open(Player player) {

        ProfileInventoryHolder holder = new ProfileInventoryHolder();

        Inventory inventory = Bukkit.createInventory(holder, 54, TITLE);

        player.openInventory(inventory);

    }

}
