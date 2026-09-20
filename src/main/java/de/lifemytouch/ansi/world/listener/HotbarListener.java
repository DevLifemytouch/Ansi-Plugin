package de.lifemytouch.ansi.world.listener;

import de.lifemytouch.ansi.cosmetic.CosmeticRegistry;
import de.lifemytouch.ansi.cosmetic.CosmeticService;
import de.lifemytouch.ansi.playtime.PlaytimeService;
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
import de.lifemytouch.ansi.coin.CoinService;
import de.lifemytouch.ansi.friend.FriendService;
import de.lifemytouch.ansi.rank.RankManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HotbarListener implements Listener {

    private final CosmeticService cosmeticService;
    private final CosmeticRegistry cosmeticRegistry;
    private final CoinService coinService;
    private final FriendService friendService;
    private final RankManager rankManager;
    private final JavaPlugin plugin;
    private final PlaytimeService playtimeService;

    public HotbarListener(
            JavaPlugin plugin,
            CosmeticService cosmeticService,
            CosmeticRegistry cosmeticRegistry,
            CoinService coinService,
            FriendService friendService,
            RankManager rankManager,
            PlaytimeService playtimeService
    ) {
        this.plugin = plugin;
        this.cosmeticService = cosmeticService;
        this.cosmeticRegistry = cosmeticRegistry;
        this.coinService = coinService;
        this.friendService = friendService;
        this.rankManager = rankManager;
        this.playtimeService = playtimeService;
    }

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
        CosmeticsInventory.open(player, cosmeticRegistry, cosmeticService);
    }

    private void onProfileClick(Player player, PlayerInteractEvent event) {
        event.setCancelled(true);
        ProfileInventory.open(
                plugin,
                player,
                rankManager,
                coinService,
                friendService,
                playtimeService
        );
    }

}
