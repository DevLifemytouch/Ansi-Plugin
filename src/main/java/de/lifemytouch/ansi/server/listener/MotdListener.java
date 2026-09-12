package de.lifemytouch.ansi.server.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener {

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMotd("§7Willkommen auf §6§lWaitinglobby's §7Testserver!");
    }

}
