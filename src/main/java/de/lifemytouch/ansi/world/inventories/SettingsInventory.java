package de.lifemytouch.ansi.world.inventories;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import de.lifemytouch.ansi.message.PrivateMessageService;
import de.lifemytouch.ansi.message.PrivateMessageSetting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import de.lifemytouch.ansi.friend.FriendService;
import de.lifemytouch.ansi.friend.FriendRequestSetting;

import java.util.List;

public class SettingsInventory {

    private static final String TITLE = "§7§lEinstellungen";

    private SettingsInventory() {
    }

    public static void open(
            Player player,
            PrivateMessageService privateMessageService,
            FriendService friendService
    ) {
        SettingsInventoryHolder holder = new SettingsInventoryHolder();

        Inventory inventory = Bukkit.createInventory(holder, 27, TITLE);

        ItemStack filler = ItemBuilder.createItem(
                Material.GRAY_STAINED_GLASS_PANE,
                " ",
                List.of()
        );

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, filler);
        }

        PrivateMessageSetting messageSetting = privateMessageService.getSetting(
                player.getUniqueId()
        );

        FriendRequestSetting requestSetting = friendService.getRequestSetting(
                player.getUniqueId()
        );

        inventory.setItem(
                11,
                ItemBuilder.createItem(
                        Material.PLAYER_HEAD,
                        "§b§lFreundschaftsanfragen",
                        List.of(
                                "§7Akzeptieren von:",
                                requestSetting.getDisplayName(),
                                "",
                                "§eKlicke zum Umschalten."
                        )
                )
        );

        inventory.setItem(
                13,
                ItemBuilder.createItem(
                        Material.WRITABLE_BOOK,
                        "§d§lPrivate Nachrichten",
                        List.of(
                                "§7Empfangen von:",
                                messageSetting.getDisplayName(),
                                "",
                                "§eKlicke zum Umschalten."
                        )
                )
        );

        inventory.setItem(
                22,
                ItemBuilder.createItem(
                        Material.ARROW,
                        "§e§lZurück",
                        List.of("§7Zurück zu deinem Profil.")
                )
        );

        player.openInventory(inventory);
    }
}