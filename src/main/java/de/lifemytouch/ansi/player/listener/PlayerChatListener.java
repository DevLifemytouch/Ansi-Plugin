package de.lifemytouch.ansi.player.listener;

import de.lifemytouch.ansi.rank.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().replace("%", "%%");

        Rank rank = Rank.getHighest(player);

        event.setFormat(rank.getPrefix() + player.getName() + ": §f" + message);

    }
}
