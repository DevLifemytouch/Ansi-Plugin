package de.lifemytouch.ansi.core.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    public static ItemStack createItem(Material material, String name, List<String> lore) {

        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createCustomHead(String value, String name, List<String> lore) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) item.getItemMeta();

        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();

        try {
            textures.setSkin(new URL(
                    "http://textures.minecraft.net/texture/" + value
            ));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        profile.setTextures(textures);
        meta.setOwnerProfile(profile);

        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }
}