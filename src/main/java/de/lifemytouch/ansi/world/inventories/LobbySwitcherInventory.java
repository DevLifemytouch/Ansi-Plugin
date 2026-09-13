package de.lifemytouch.ansi.world.inventories;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class LobbySwitcherInventory {

    private static final String TITLE = "§d§lLobby Switcher";

    public static void open(Player player) {

        LobbySwitcherInventoryHolder holder = new LobbySwitcherInventoryHolder();

        Inventory inventory = Bukkit.createInventory(holder, 9, TITLE);

        inventory.setItem(0, ItemBuilder.createCustomHead(
                "fcfe8845a8d5e635fb87728ccc93895d42b4fc2e6a53f1ba78c845225822",
                "§c§lSilent Lobby-1",
                List.of("§7" + "0"+ "/" + "0")
        ));

        inventory.setItem(1, ItemBuilder.createCustomHead(
                "4ef356ad2aa7b1678aecb88290e5fa5a3427e5e456ff42fb515690c67517b8",
                "§a§lLobby-1",
                List.of("§7" + player.getServer().getOnlinePlayers().size()+ "/" + player.getServer().getMaxPlayers())
        ));

        player.openInventory(inventory);

    }

}
