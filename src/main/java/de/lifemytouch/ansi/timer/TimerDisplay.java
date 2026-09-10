package de.lifemytouch.ansi.timer;

import de.lifemytouch.ansi.core.text.GradientUtil;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;

public class TimerDisplay {
    private final JavaPlugin plugin;
    private final TimerManager timerManager;

    private final Color start = new Color(0, 105, 130);
    private final Color end   = new Color(94, 234, 255);

    private static final float SHINE_WIDTH = 2f;


    private static final float SHINE_SPEED = 0.2f;

    private BukkitTask task;
    private float shinePosition = -SHINE_WIDTH;

    public TimerDisplay(JavaPlugin plugin, TimerManager timerManager) {
        this.plugin = plugin;
        this.timerManager = timerManager;
    }

    public void start() {
        if (task != null) return;

        task = new BukkitRunnable() {

            @Override
            public void run() {
                String time = timerManager.getFormattedTime();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            GradientUtil.createShineGradient(time, start, end, shinePosition, SHINE_WIDTH)
                    );
                }

                shinePosition += SHINE_SPEED;
                if (shinePosition > time.length() + SHINE_WIDTH) {
                    shinePosition = -SHINE_WIDTH;
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}