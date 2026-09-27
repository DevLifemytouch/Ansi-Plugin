package de.lifemytouch.ansi.anticheat.check.movement;

import de.lifemytouch.ansi.anticheat.AntiCheatManager;
import de.lifemytouch.ansi.anticheat.check.Check;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoFallCheck extends Check implements Listener {

    private static final double MIN_FALL_DISTANCE = 4.0;
    private final Map<UUID, Double> fallDistances = new HashMap<>();
    private final Map<UUID, Long> lastFallDamage = new HashMap<>();

    public NoFallCheck(AntiCheatManager antiCheatManager) {
        super(antiCheatManager);
    }

    @Override
    public String getName() {
        return "NoFall";
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(!isEnabled()) return;
        if(event instanceof PlayerTeleportEvent) return;
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

        double deltaY = to.getY() - from.getY();
        UUID uuid = player.getUniqueId();

        if(deltaY < 0.0) {
            double current = fallDistances.getOrDefault(uuid, 0.0);

            fallDistances.put(uuid, current + Math.abs(deltaY));

            return;
        }

        if(deltaY > 0.0) {
            fallDistances.remove(uuid);
            return;
        }

        if(player.isOnGround()) {
            checkLanding(player);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player player)) return;
        if(!isEnabled()) return;
        if(event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        UUID uuid = player.getUniqueId();
        lastFallDamage.put(uuid, System.currentTimeMillis());

        fallDistances.remove(uuid);
    }

    private void checkLanding(Player player) {
        UUID uuid = player.getUniqueId();

        double fallDistance = fallDistances.getOrDefault(uuid, 0.0);

        if(fallDistance < MIN_FALL_DISTANCE) {
            fallDistances.remove(uuid);
            return;
        }

        long lastDamage = lastFallDamage.getOrDefault(uuid, 0L);

        long timeSinceDamage = System.currentTimeMillis() - lastDamage;

        if(timeSinceDamage < 100L) {
            fallDistances.remove(uuid);
            return;
        }

        if(!hasNormalLandingBlock(player)) {
            fallDistances.remove(uuid);
            return;
        }

        if(isSpecialLandingBlock(player)) {
            fallDistances.remove(uuid);
            return;
        }

        flag(player, 1.0, String.format("%.2f", fallDistance) + " Blöcke.");
        fallDistances.remove(uuid);
    }

    private boolean hasNormalLandingBlock(Player player) {
        Location location = player.getLocation();

        Block block = location.clone().subtract(0, 0.15, 0).getBlock();

        return block.getType().isSolid();
    }

    private boolean isSpecialLandingBlock(Player player) {

        Location location = player.getLocation();
        Block block = location.clone().subtract(0, 0.15, 0).getBlock();
        Material material = block.getType();

        return material == Material.SLIME_BLOCK
                || material == Material.HAY_BLOCK
                || material == Material.HONEY_BLOCK
                || material == Material.WATER
                || material == Material.LAVA
                || material == Material.POWDER_SNOW
                || material == Material.COBWEB;

    }

    private boolean shouldIgnore(Player player) {
        GameMode gameMode = player.getGameMode();

        return gameMode == GameMode.CREATIVE
                || gameMode == GameMode.SPECTATOR
                || player.isFlying()
                || player.isGliding()
                || player.isInsideVehicle()
                || player.isSwimming()
                || player.isInWater();
    }

    private void reset(Player player) {
        UUID uuid = player.getUniqueId();

        fallDistances.remove(uuid);
        lastFallDamage.remove(uuid);
    }

}
