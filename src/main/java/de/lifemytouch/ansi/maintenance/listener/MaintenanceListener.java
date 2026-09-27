package de.lifemytouch.ansi.maintenance.listener;

import de.lifemytouch.ansi.maintenance.MaintenanceManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MaintenanceListener implements Listener {

    private final MaintenanceManager maintenanceManager;

    public MaintenanceListener(MaintenanceManager maintenanceManager) {
        this.maintenanceManager = maintenanceManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {

        if (!maintenanceManager.isEnabled()) {
            return;
        }

        if (maintenanceManager.isAllowed(event.getPlayer())) {
            return;
        }

        event.getPlayer().kickPlayer(
                "§6§lANSI\n\n"
                        + "§cDer Server befindet sich derzeit\n"
                        + "§cin Wartungsarbeiten.\n\n"
                        + "§7Bitte versuche es später erneut."
        );

        event.setJoinMessage("");
    }
}