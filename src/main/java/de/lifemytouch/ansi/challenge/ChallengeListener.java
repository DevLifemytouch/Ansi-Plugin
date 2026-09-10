package de.lifemytouch.ansi.challenge;

import de.lifemytouch.ansi.challenge.item.ItemChallengeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class ChallengeListener implements Listener {

    private final ItemChallengeManager itemChallengeManager;

    public ChallengeListener(ItemChallengeManager itemChallengeManager) {
        this.itemChallengeManager = itemChallengeManager;
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        itemChallengeManager.scheduleCheck(player);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        itemChallengeManager.scheduleCheck(player);
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        itemChallengeManager.scheduleCheck(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        itemChallengeManager.scheduleCheck(player);
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        itemChallengeManager.scheduleCheck(event.getPlayer());
    }

}
