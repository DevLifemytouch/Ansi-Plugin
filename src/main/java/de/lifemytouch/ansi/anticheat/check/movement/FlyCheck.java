package de.lifemytouch.ansi.anticheat.check.movement;

import de.lifemytouch.ansi.anticheat.AntiCheatManager;
import de.lifemytouch.ansi.anticheat.check.Check;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlyCheck extends Check implements Listener {

    private static final int MAX_AIR_TICKS = 25;
    private static final double MAX_HOVER_MOVEMENT = 0.03;

    private final Map<UUID, Integer> airTicks = new HashMap<>();
    private final Map<UUID, Integer> suspiciousTicks = new HashMap<>();

    public FlyCheck(AntiCheatManager antiCheatManager) {
        super(antiCheatManager);
    }

    @Override
    public String getName() {
        return "Fly";
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(!isEnabled()) return;
        if(event instanceof PlayerTeleportEvent) {
            reset(player);
            return;
        }
        if(event.getTo() == null) return;
        if(shouldIgnore(player)) {
            reset(player);
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getWorld() == null || to.getWorld() == null || !from.getWorld().equals(to.getWorld())) {
            reset(player);
            return;
        }

        UUID uuid = player.getUniqueId();

        double deltaY = to.getY() - from.getY();

        if(player.isOnGround()) {
            reset(player);
            return;
        }

        int currentAirTicks = airTicks.getOrDefault(uuid, 0) + 1;

        airTicks.put(uuid, currentAirTicks);

        if(currentAirTicks > MAX_AIR_TICKS) {
            if(Math.abs(deltaY) <= MAX_HOVER_MOVEMENT) {
                int suspicious = suspiciousTicks.getOrDefault(uuid, 0) + 1;

                suspiciousTicks.put(uuid, suspicious);

                if(suspicious >= 5) {
                    flag(player, 1.0, String.format("%.3f", deltaY) + " Y-Bewegung, " + currentAirTicks + "air ticks");
                    suspiciousTicks.put(uuid, 0);
                }
            } else {
                suspiciousTicks.remove(uuid);
            }
        }
    }

    private boolean shouldIgnore(Player player) {
        GameMode gameMode = player.getGameMode();

        if(gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) return true;

        if(player.isFlying()
                || player.isGliding()
                || player.isInsideVehicle()
                || player.isSwimming()
                || player.isInWater()) return true;

        return false;
    }

    private void reset(Player player) {
        UUID uuid = player.getUniqueId();

        airTicks.remove(uuid);
        suspiciousTicks.remove(uuid);
    }
}
