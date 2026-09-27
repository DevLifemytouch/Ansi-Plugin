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

public class SpeedCheck extends Check implements Listener {

    private static final double MAX_SPEED = 0.55;
    private static final double SPRINT_SPEED = 0.70;

    private final Map<UUID, Integer> suspiciousMoves = new HashMap<>();

    public SpeedCheck(AntiCheatManager antiCheatManager) {
        super(antiCheatManager);
    }

    @Override
    public String getName() {
        return "Speed";
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(!isEnabled()) return;
        if(event instanceof PlayerTeleportEvent) return;
        if(event.getTo() == null) return;
        if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if(player.isFlying()) return;
        if(player.isInsideVehicle()) return;
        if(player.isGliding()) return;
        if(player.isSwimming() || player.isInWater()) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getWorld() == null || to.getWorld() == null) return;
        if(!from.getWorld().equals(to.getWorld())) return;

        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();

        double horizontalDistance = Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));

        if(horizontalDistance <= 0.0) {
            return;
        }

        double allowedSpeed = player.isSprinting() ? SPRINT_SPEED : MAX_SPEED;

        if(horizontalDistance > allowedSpeed) {
            int violations = suspiciousMoves.merge(player.getUniqueId(), 1, Integer::sum);

            if(violations >= 3) {
                flag(player, 1.0, "§7Horizontal Speed: "
                        + String.format("§c%.3f", horizontalDistance)
                        + " §7> "
                        + String.format("§c%.3f", allowedSpeed)
                        + "§7(§cSpeed-A§7)."
                );
            }
        }
    }
}
