package de.lifemytouch.ansi.world.listener;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.world.inventories.*;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInteract(InventoryClickEvent event) {

        if(!(event.getWhoClicked() instanceof Player player)) return;

        if(event.getView().getTopInventory().getHolder() instanceof NavInventoryHolder holder) {
            event.setCancelled(true);

            handleNavInventoryClick(player, event, holder);
        }

        if(event.getView().getTopInventory().getHolder() instanceof CosmeticsInventoryHolder holder) {
            event.setCancelled(true);

            handleCosmeticsInventoryClick(player, event, holder);
        }

        if(event.getView().getTopInventory().getHolder() instanceof CosmeticsShopInventoryHolder holder) {
            event.setCancelled(true);

            handleCosmeticsShopInventoryClick(player, event, holder);
        }

    }

    private void handleNavInventoryClick(Player player, InventoryClickEvent event, NavInventoryHolder holder) {
        int slot = event.getRawSlot();
        World world = player.getWorld();

        switch (slot) {
            case 10 -> {
                player.teleport(new Location(world, -1424.5, 28, 897.5, 45, -15));
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                player.sendMessage(Messages.getPREFIX() + "§7Du wurdest zum PVP-NPC teleportiert!");
            }
            case 13 -> {
                player.teleport(new Location(player.getWorld(), -1487.5, 35, 854.5, 0,0));
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                player.sendMessage(Messages.getPREFIX() + "§7Du wurdest zum Spawn teleportiert!");
            }
            case 16 -> {
                player.teleport(new Location(world, -1576.5, 28, 886.5, 90, -15));
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                player.sendMessage(Messages.getPREFIX() + "§7Du wurdest zum SMP-NPC teleportiert!");
            }
            default -> {}
        }

    }

    private void handleCosmeticsInventoryClick(Player player,
                                               InventoryClickEvent event, CosmeticsInventoryHolder holder) {
        int slot = event.getRawSlot();

        switch (slot) {
            case 45 -> {
                CosmeticsShopInventory.open(player);
            }
            case 49 -> {
                player.sendMessage(Messages.getPREFIX() + "§cBald...");
            }
            case 53 -> {
                player.sendMessage(Messages.getPREFIX() + "§7Keine Seite vorhanden");}
            default -> {}
        }
    }

    private void handleCosmeticsShopInventoryClick(Player player,
                                               InventoryClickEvent event, CosmeticsShopInventoryHolder holder) {
        int slot = event.getRawSlot();

        switch (slot) {
            case 45 -> {
                CosmeticsInventory.open(player);
            }
            case 49 -> {
                player.sendMessage(Messages.getPREFIX() + "§cBald...");
            }
            case 53 -> {
                player.sendMessage(Messages.getPREFIX() + "§7Keine Seite vorhanden");}
            default -> {}
        }
    }

}
