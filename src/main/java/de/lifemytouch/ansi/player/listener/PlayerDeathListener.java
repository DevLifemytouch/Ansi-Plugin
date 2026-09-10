package de.lifemytouch.ansi.player.listener;

import de.lifemytouch.ansi.Ansi;
import de.lifemytouch.ansi.timer.TimerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDeathListener implements Listener {

    private TimerManager timerManager;

    private final Map<UUID, Location> deathLocations = new HashMap<>();

    public PlayerDeathListener(TimerManager timerManager) {
        this.timerManager = timerManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        deathLocations.put(
                player.getUniqueId(),
                player.getLocation().clone()
        );

        event.setDeathMessage("");
        player.sendMessage(Ansi.getPREFIX() + "§7Du bist §cgestorben§7!");
        for(Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(Ansi.getPREFIX() + "§7Der §6Timer §7wurde §cpausiert§7.");
            players.setGameMode(GameMode.SPECTATOR);

            if(players == player) continue;
            players.sendMessage(Ansi.getPREFIX() + "§c" + player.getName() + "§7 ist §cgestorben§7!");
        }

        timerManager.pause();

    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Location deathLocation = deathLocations.get(player.getUniqueId());

        if(deathLocation == null) return;

        event.setRespawnLocation(deathLocation);
        deathLocations.remove(player.getUniqueId());
    }
}
