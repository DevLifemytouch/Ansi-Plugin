package de.lifemytouch.ansi.timer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class TimerManager {

    private final JavaPlugin plugin;
    private final File dataFile;

    private long elapsedSeconds = 0;
    private boolean running = false;

    public TimerManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "timer.yml");
        load();
    }

    private void load() {
        if (!dataFile.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        elapsedSeconds = config.getLong("elapsedSeconds", 0);
        running = config.getBoolean("running", false);
    }

    public void save() {
        FileConfiguration config = new YamlConfiguration();
        config.set("elapsedSeconds", elapsedSeconds);
        config.set("running", running);

        try {
            config.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Konnte timer.yml nicht speichern: " + e.getMessage());
        }
    }

    public void start() {
        running = true;
    }

    public void pause() {
        running = false;
    }

    public void reset() {
        elapsedSeconds = 0;
        running = true;
    }

    public void stop() {
        elapsedSeconds = 0;
        running = false;
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void tick() {
        if (running) {
            elapsedSeconds++;
        }
    }

    public String getFormattedTime() {
        long totalSeconds = elapsedSeconds;

        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        }

        if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        }

        return String.format("%ds", seconds);
    }

    public boolean isRunning() {
        return running;
    }
}
