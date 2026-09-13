package de.lifemytouch.ansi.world.inventories;

import de.lifemytouch.ansi.core.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class LobbyHotbar {

    private static final String TITLE = "§c§lKompass";

    public static void setSpawnItems(Player player) {

        Inventory inventory = player.getInventory();

        inventory.setItem(0, ItemBuilder.createCustomHead(
                "cc1b2f592cfc8d372dcf5fd44eed69dddc64601d7846d72619f70511d8043a89",
                "§6§lCosmetics",
                List.of("§7Öffnet die Cosmetics")
        ));


        inventory.setItem(4, ItemBuilder.createItem(
                Material.COMPASS,
                "§c§lNavigator",
                List.of(
                        "§7Öffnet die den Navigator!"
                )
        ));
        inventory.setItem(7, ItemBuilder.createItem(
                Material.NETHER_STAR,
                "§d§lLobby Switcher",
                List.of(
                        "§7Öffnet die Serverauswahl!"
                )
        ));

        ItemStack profile = ItemBuilder.createItem(
                Material.PLAYER_HEAD,
                "§9§lProfil",
                List.of(
                        "§7Öffnet dein Profil!"
                )
        );

        SkullMeta meta = (SkullMeta) profile.getItemMeta();
        meta.setOwningPlayer(player);
        profile.setItemMeta(meta);

        inventory.setItem(8, profile);

    }

}
