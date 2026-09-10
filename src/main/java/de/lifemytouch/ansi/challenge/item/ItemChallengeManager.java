package de.lifemytouch.ansi.challenge.item;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ItemChallengeManager {

    private final JavaPlugin plugin;
    private final File dataFile;

    private boolean active = false;
    private BossBar bossBar;

    private final List<Material> allItems = new ArrayList<>();
    private final Deque<Material> queue = new ArrayDeque<>();
    private final Set<Material> collected = new HashSet<>();
    private Material currentTarget;

    public ItemChallengeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "challenge.yml");

        this.allItems.addAll(buildItemPool());
        loadState();
    }

    private List<Material> buildItemPool() {
        List<Material> pool = new ArrayList<>();
        for (Material material : Material.values()) {
            if (isValidChallengeItem(material)) {
                pool.add(material);
            }
        }
        return pool;
    }

    private boolean isValidChallengeItem(Material material) {
        if (!material.isItem()) return false;
        if (material.isLegacy()) return false;
        if (material == Material.AIR) return false;

        return switch (material) {
            case COMMAND_BLOCK, CHAIN_COMMAND_BLOCK, REPEATING_COMMAND_BLOCK,
                 BARRIER, STRUCTURE_BLOCK, STRUCTURE_VOID, JIGSAW,
                 DEBUG_STICK, KNOWLEDGE_BOOK, SPAWNER, LIGHT -> false;
            default -> true;
        };
    }

    private void loadState() {
        if (!dataFile.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);

        active = config.getBoolean("active", false);

        String targetName = config.getString("target");
        currentTarget = targetName != null ? Material.matchMaterial(targetName) : null;

        queue.clear();
        for (String name : config.getStringList("queue")) {
            Material material = Material.matchMaterial(name);
            if (material != null) queue.add(material);
        }

        collected.clear();
        for (String name : config.getStringList("collected")) {
            Material material = Material.matchMaterial(name);
            if (material != null) collected.add(material);
        }

        if (active) {
            setupBossBar();
            updateBossBar();
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
            plugin.getLogger().severe("Konnte challenge.yml nicht speichern: " + e.getMessage());
        }
    }

    public void start(Player starter) {
        this.active = true;
        this.collected.clear();
        this.queue.clear();

        List<Material> shuffled = new ArrayList<>(allItems);
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
        bossBar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
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

        String itemName = formatMaterialName(currentTarget);
        bossBar.setTitle("§7Nächstes Item: §6§l" + itemName + " §7(" + collected.size() + "/" + allItems.size() + ")");
        bossBar.setProgress(Math.min(1.0, (double) collected.size() / allItems.size()));
    }

    private String formatMaterialName(Material material) {
        String[] words = material.name().split("_");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            builder.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
        }
        return builder.toString().trim();
    }

    private void finishChallenge() {
        active = false;
        bossBar.setTitle("§6§lAlle Items gesammelt!");
        bossBar.setProgress(1.0);

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.sendMessage("§6§lGlückwunsch! §7Alle Items wurden gesammelt!");
        }
    }

    public void scheduleCheck(Player player) {
        if (!active) return;

        Bukkit.getScheduler().runTask(plugin, () -> checkInventory(player));
    }

    private void checkInventory(Player player) {
        if (!active || currentTarget == null) return;
        if (!player.getInventory().contains(currentTarget)) return;

        onTargetObtained(player);
    }

    private void onTargetObtained(Player player) {
        collected.add(currentTarget);
        playProgressSound();
        Bukkit.broadcastMessage("§6§l" + player.getName() + " §7hat §6§l" + formatMaterialName(currentTarget) + " §7gefunden!");
        pickNextTarget();
        saveState();
    }

    private void playProgressSound() {
        float progress = allItems.isEmpty() ? 0f : (float) collected.size() / allItems.size();
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

    public Material getCurrentTarget() {
        return currentTarget;
    }

    public int getCollectedCount() {
        return collected.size();
    }

    public int getTotalCount() {
        return allItems.size();
    }
}