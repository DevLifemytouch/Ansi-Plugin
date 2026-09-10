package de.lifemytouch.ansi.challenge.mob;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MobChallengeManager {

    private final JavaPlugin plugin;
    private final File dataFile;

    private boolean active = false;
    private BossBar bossBar;

    private final List<EntityType> allMobs = new ArrayList<>();
    private final Deque<EntityType> queue = new ArrayDeque<>();
    private final Set<EntityType> collected = new HashSet<>();
    private EntityType currentTarget;

    public MobChallengeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "mobchallenge.yml");

        this.allMobs.addAll(buildMobPool());
        loadState();
    }

    private List<EntityType> buildMobPool() {
        List<EntityType> pool = new ArrayList<>();
        for (EntityType type : EntityType.values()) {
            if (isValidChallengeMob(type)) {
                pool.add(type);
            }
        }
        return pool;
    }

    private boolean isValidChallengeMob(EntityType type) {
        if (type == EntityType.PLAYER) return false;
        if (type == EntityType.UNKNOWN) return false;

        return switch (type) {
            case ARMOR_STAND, ITEM_FRAME, GLOW_ITEM_FRAME, PAINTING,
                 MARKER, INTERACTION, MANNEQUIN -> false;
            default -> {
                Class<?> entityClass = type.getEntityClass();
                yield entityClass != null && LivingEntity.class.isAssignableFrom(entityClass);
            }
        };
    }

    private void loadState() {
        if (!dataFile.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);

        active = config.getBoolean("active", false);

        String targetName = config.getString("target");
        currentTarget = targetName != null ? safeValueOf(targetName) : null;

        queue.clear();
        for (String name : config.getStringList("queue")) {
            EntityType type = safeValueOf(name);
            if (type != null) queue.add(type);
        }

        collected.clear();
        for (String name : config.getStringList("collected")) {
            EntityType type = safeValueOf(name);
            if (type != null) collected.add(type);
        }

        if (active) {
            setupBossBar();
            updateBossBar();
        }
    }

    private EntityType safeValueOf(String name) {
        try {
            return EntityType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void saveState() {
        FileConfiguration config = new YamlConfiguration();

        config.set("active", active);
        config.set("target", currentTarget != null ? currentTarget.name() : null);
        config.set("queue", queue.stream().map(Enum::name).collect(Collectors.toList()));
        config.set("collected", collected.stream().map(Enum::name).collect(Collectors.toList()));

        try {
            config.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Konnte mobchallenge.yml nicht speichern: " + e.getMessage());
        }
    }

    public void start(Player starter) {
        this.active = true;
        this.collected.clear();
        this.queue.clear();

        List<EntityType> shuffled = new ArrayList<>(allMobs);
        Collections.shuffle(shuffled);
        queue.addAll(shuffled);

        setupBossBar();
        pickNextTarget();
        saveState();
    }

    public void stop() {
        active = false;
        if (bossBar != null) {
            bossBar.removeAll();
        }
        saveState();
    }

    private void setupBossBar() {
        if (bossBar != null) {
            bossBar.removeAll();
        }
        bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
        for (Player online : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(online);
        }
    }

    public void addPlayer(Player player) {
        if (active && bossBar != null) {
            bossBar.addPlayer(player);
        }
    }

    public void removePlayer(Player player) {
        if (bossBar != null) {
            bossBar.removePlayer(player);
        }
    }

    public void shutdown() {
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    private void pickNextTarget() {
        if (queue.isEmpty()) {
            finishChallenge();
            return;
        }

        currentTarget = queue.poll();
        updateBossBar();
    }

    private void updateBossBar() {
        if (bossBar == null || currentTarget == null) return;

        String mobName = formatEntityName(currentTarget);
        bossBar.setTitle("§7Nächstes Mob: §6§l" + mobName + " §7(" + collected.size() + "/" + allMobs.size() + ")");
        bossBar.setProgress(Math.min(1.0, (double) collected.size() / allMobs.size()));
    }

    private String formatEntityName(EntityType type) {
        String[] words = type.name().split("_");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            builder.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
        }
        return builder.toString().trim();
    }

    private void finishChallenge() {
        active = false;
        bossBar.setTitle("§a§lAlle Mobs besiegt!");
        bossBar.setProgress(1.0);

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.sendMessage("§a§lGlückwunsch! Alle Mobs wurden besiegt!");
        }
    }

    public void onMobKilled(EntityType type, Player killer) {
        if (!active || currentTarget == null) return;
        if (type != currentTarget) return;

        collected.add(currentTarget);
        playProgressSound();
        Bukkit.broadcastMessage("§6§l" + killer.getName() + " §7hat §f" + formatEntityName(currentTarget) + " §7getötet!");
        pickNextTarget();
        saveState();
    }

    private void playProgressSound() {
        float progress = allMobs.isEmpty() ? 0f : (float) collected.size() / allMobs.size();
        float pitch = 0.5f + progress * 1.5f;
        pitch = Math.max(0.5f, Math.min(2.0f, pitch));

        for (Player online : Bukkit.getOnlinePlayers()) {
            Location loc = online.getLocation();
            online.playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, pitch);
        }
    }

    public boolean isActive() {
        return active;
    }

    public EntityType getCurrentTarget() {
        return currentTarget;
    }

    public int getCollectedCount() {
        return collected.size();
    }

    public int getTotalCount() {
        return allMobs.size();
    }
}