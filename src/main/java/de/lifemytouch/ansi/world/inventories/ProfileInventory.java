package de.lifemytouch.ansi.world.inventories;

import de.lifemytouch.ansi.coin.CoinService;
import de.lifemytouch.ansi.core.item.ItemBuilder;
import de.lifemytouch.ansi.friend.FriendService;
import de.lifemytouch.ansi.rank.Rank;
import de.lifemytouch.ansi.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerProfile;
import de.lifemytouch.ansi.playtime.PlaytimeService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ProfileInventory {

    private static final String TITLE = "§b§lProfil";

    private ProfileInventory() {
    }

    public static void open(
            JavaPlugin plugin,
            Player player,
            RankManager rankManager,
            CoinService coinService,
            FriendService friendService,
            PlaytimeService playtimeService
    ) {
        ProfileInventoryHolder holder = new ProfileInventoryHolder();

        Inventory inventory = Bukkit.createInventory(holder, 54, TITLE);

        ItemStack filler = ItemBuilder.createItem(
                Material.GRAY_STAINED_GLASS_PANE,
                " ",
                List.of()
        );

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, filler);
        }

        Rank rank = rankManager.getRank(player);
        long coins = coinService.getCoins(player.getUniqueId());

        List<UUID> friends = new ArrayList<>(
                friendService.getFriends(player.getUniqueId())
        );

        friends.sort(
                Comparator.comparing(
                        friendId -> Bukkit.getPlayer(friendId) == null
                )
        );

        int onlineFriends = (int) friends.stream()
                .filter(friendId -> Bukkit.getPlayer(friendId) != null)
                .count();

        long playtime = playtimeService.getPlaytime(player.getUniqueId());

        inventory.setItem(
                2,
                ItemBuilder.createItem(
                        Material.CLOCK,
                        "§b§lSpielzeit",
                        List.of(
                                "§7Deine gesamte Spielzeit:",
                                "",
                                "§b" + playtimeService.format(playtime)
                        )
                )
        );

        inventory.setItem(
                4,
                createPlayerHead(
                        player,
                        "§b§l" + player.getName(),
                        List.of(
                                "§7Rang: " + rank.getPrefix(),
                                "§7Coins: §e" + coins,
                                "§7Freunde: §b" + friends.size(),
                                "§7Freunde online: §a" + onlineFriends
                        )
                )
        );

        inventory.setItem(
                6,
                ItemBuilder.createItem(
                        Material.SUNFLOWER,
                        "§e§lCoins",
                        List.of(
                                "§7Dein Kontostand:",
                                "",
                                "§e" + coins + " Coins"
                        )
                )
        );

        if (friends.isEmpty()) {
            inventory.setItem(
                    31,
                    ItemBuilder.createItem(
                            Material.BARRIER,
                            "§c§lKeine Freunde",
                            List.of(
                                    "§7Du hast noch keine Freunde.",
                                    "",
                                    "§7Nutze §b/friend add <Spieler>"
                            )
                    )
            );
        } else {
            int maxFriends = Math.min(friends.size(), 7);

            for (int index = 0; index < maxFriends; index++) {
                UUID friendId = friends.get(index);
                OfflinePlayer friend = Bukkit.getOfflinePlayer(friendId);
                Player onlineFriend = Bukkit.getPlayer(friendId);

                String name = friend.getName() == null
                        ? friendId.toString().substring(0, 8)
                        : friend.getName();

                boolean online = onlineFriend != null;

                int slot = 28 + index;

                if (online) {
                    inventory.setItem(
                            slot,
                            createPlayerHead(
                                    friend,
                                    "§a§l" + name,
                                    List.of("§7Status: §aOnline")
                            )
                    );
                } else {
                    setOfflinePlayerHead(
                            plugin,
                            inventory,
                            slot,
                            friend,
                            "§7§l" + name,
                            List.of("§7Status: §cOffline")
                    );
                }
            }
        }

        inventory.setItem(
                45,
                createPlayerHead(
                        player,
                        "§b§lFreunde",
                        List.of(
                                "§7Online: §a" + onlineFriends,
                                "§7Gesamt: §b" + friends.size(),
                                "",
                                "§7Verwalte Freunde mit",
                                "§b/friend"
                        )
                )
        );

        inventory.setItem(
                49,
                ItemBuilder.createItem(
                        Material.GOLDEN_HELMET,
                        "§6§lCosmetics",
                        List.of(
                                "§7Öffne deine Cosmetics."
                        )
                )
        );

        inventory.setItem(
                53,
                ItemBuilder.createItem(
                        Material.COMPARATOR,
                        "§7§lEinstellungen",
                        List.of(
                                "§7Verwalte deine",
                                "§7persönlichen Einstellungen.",
                                "",
                                "§eKlicke zum Öffnen."
                        )
                )
        );

        player.openInventory(inventory);
    }

    private static ItemStack createPlayerHead(
            OfflinePlayer owner,
            String name,
            List<String> lore
    ) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(owner);
        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    private static void setOfflinePlayerHead(
            JavaPlugin plugin,
            Inventory inventory,
            int slot,
            OfflinePlayer owner,
            String name,
            List<String> lore
    ) {
        ItemStack item = createPlayerHead(owner, name, lore);
        inventory.setItem(slot, item);

        PlayerProfile profile = Bukkit.createPlayerProfile(owner.getUniqueId());

        profile.update().thenAccept(updatedProfile ->
                Bukkit.getScheduler().runTask(plugin, () -> {
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    meta.setOwnerProfile(updatedProfile);
                    item.setItemMeta(meta);

                    inventory.setItem(slot, item);
                })
        );
    }
}