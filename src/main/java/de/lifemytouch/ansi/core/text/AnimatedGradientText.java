package de.lifemytouch.ansi.core.text;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.awt.Color;
import java.util.function.Consumer;

public class AnimatedGradientText {

    private final JavaPlugin plugin;
    private final String text;
    private final Color start;
    private final Color end;
    private final float shineWidth;
    private final float speed;

    private Consumer<String> onUpdate;
    private BukkitTask task;
    private float position;

    /**
     * @param shineWidth Breite des Glanzes in Zeichen - größer = weicher/ruhiger.
     * @param speed      Wie viele Zeichen der Glanz pro Tick wandert - kleiner = langsamer.
     *                   Ein Wert um 0.05-0.1 wirkt für die meisten Texte angenehm ruhig.
     */
    public AnimatedGradientText(JavaPlugin plugin, String text, Color start, Color end, float shineWidth, float speed) {
        this.plugin = plugin;
        this.text = text;
        this.start = start;
        this.end = end;
        this.shineWidth = shineWidth;
        this.speed = speed;
        this.position = -shineWidth;
    }

    public AnimatedGradientText onUpdate(Consumer<String> onUpdate) {
        this.onUpdate = onUpdate;
        return this;
    }

    public void start() {
        if (task != null) return;

        task = new BukkitRunnable() {
            @Override
            public void run() {
                String formatted = GradientUtil.toLegacyText(
                        GradientUtil.createShineGradient(text, start, end, position, shineWidth)
                );

                if (onUpdate != null) {
                    onUpdate.accept(formatted);
                }

                position += speed;
                if (position > text.length() + shineWidth) {
                    position = -shineWidth;
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