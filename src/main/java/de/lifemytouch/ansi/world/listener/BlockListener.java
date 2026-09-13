package de.lifemytouch.ansi.world.listener;

import de.lifemytouch.ansi.build.BuildService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class BlockListener implements Listener {

    private BuildService buildService;

    public BlockListener(BuildService buildService) {
        this.buildService = buildService;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();

        if(buildService.buildList.contains(player)) {
            event.setCancelled(false);
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();

        if(buildService.buildList.contains(player)) {
            event.setCancelled(false);
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {

        Player player = event.getPlayer();

        if(buildService.buildList.contains(player)) {
            event.setCancelled(false);
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if(buildService.buildList.contains(player)) {
            event.setCancelled(false);
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if(!buildService.buildList.contains(player)) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        Player player = event.getPlayer();

        if(!buildService.buildList.contains(player)) {
            event.setCancelled(true);
        }

    }

}