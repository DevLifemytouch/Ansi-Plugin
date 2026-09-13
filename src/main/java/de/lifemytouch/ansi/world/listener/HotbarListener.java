package de.lifemytouch.ansi.world.listener;

import de.lifemytouch.ansi.world.inventories.CosmeticsInventory;
import de.lifemytouch.ansi.world.inventories.LobbySwitcherInventory;
import de.lifemytouch.ansi.world.inventories.NavInventory;
import de.lifemytouch.ansi.world.inventories.ProfileInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HotbarListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();

        if(item == null) return;

        if(item.getType() == Material.COMPASS) {
            onCompassClick(player, event);
            return;
        }
        if(item.getType() == Material.PLAYER_HEAD && item.getItemMeta().getDisplayName().equals("§6§lCosmetics")) {
            onCosmeticsClick(player, event);
            return;
        }
        if(item.getType() == Material.NETHER_STAR) {
            onLobbySwitcherClick(player, event);
            return;
        }
        if(item.getType() == Material.PLAYER_HEAD && item.getItemMeta().getDisplayName().equals("§9§lProfil")) {
            onProfileClick(player, event);
        }

    }

    private void onCompassClick(Player player, PlayerInteractEvent event) {
        event.setCancelled(true);
        NavInventory.open(player);
    }

    private void onLobbySwitcherClick(Player player, PlayerInteractEvent event) {
        event.setCancelled(true);
        LobbySwitcherInventory.open(player);
    }

    private void onCosmeticsClick(Player player, PlayerInteractEvent event) {
        event.setCancelled(true);
        CosmeticsInventory.open(player);
    }

    private void onProfileClick(Player player, PlayerInteractEvent event) {
        event.setCancelled(true);
        ProfileInventory.open(player);
    }

}
