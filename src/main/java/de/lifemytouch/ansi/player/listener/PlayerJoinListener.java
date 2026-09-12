package de.lifemytouch.ansi.player.listener;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.rank.RankManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.function.Consumer;

public class PlayerJoinListener implements Listener {

    private final RankManager rankManager;

    private final Consumer<Player> updateTabList;

    public PlayerJoinListener(RankManager rankManager, Consumer<Player> updateTabList) {
        this.rankManager = rankManager;
        this.updateTabList = updateTabList;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        rankManager.applyPermission(player);
        updateTabList.accept(player);

        player.sendMessage(Messages.getPREFIX() + "§7Willkommen auf " + Messages.getANSI_GRADIENT() + "'s §7Server!");
        event.setJoinMessage("");
    }
}
