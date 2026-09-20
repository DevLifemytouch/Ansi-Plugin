package de.lifemytouch.ansi.staff;

import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffChatListener implements Listener {

    private final JavaPlugin plugin;
    private final StaffChatService staffChatService;

    public StaffChatListener(
            JavaPlugin plugin,
            StaffChatService staffChatService
    ) {
        this.plugin = plugin;
        this.staffChatService = staffChatService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();

        if (!staffChatService.isActive(sender.getUniqueId())) {
            return;
        }

        event.setCancelled(true);

        String message = event.getMessage();

        Bukkit.getScheduler().runTask(plugin, () -> {
            for (Player receiver : Bukkit.getOnlinePlayers()) {
                if (!receiver.hasPermission("ansi.commands.sc")) {
                    continue;
                }

                receiver.sendMessage(
                        "§8[§b§lSTAFF§8] "
                                + "§7" + sender.getName()
                                + "§8: §f" + message
                );
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        staffChatService.disable(event.getPlayer().getUniqueId());
    }
}