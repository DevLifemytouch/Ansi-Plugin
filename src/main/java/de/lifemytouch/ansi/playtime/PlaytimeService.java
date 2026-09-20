package de.lifemytouch.ansi.playtime;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaytimeService {

    private final PlaytimeRepository repository;
    private final Map<UUID, Long> sessionStarts = new HashMap<>();

    public PlaytimeService(PlaytimeRepository repository) {
        this.repository = repository;
    }

    public void start(Player player) {
        sessionStarts.putIfAbsent(
                player.getUniqueId(),
                System.currentTimeMillis()
        );
    }

    public void stop(Player player) {
        UUID playerId = player.getUniqueId();
        Long sessionStart = sessionStarts.remove(playerId);

        if (sessionStart == null) {
            return;
        }

        repository.addPlaytime(
                playerId,
                System.currentTimeMillis() - sessionStart
        );
    }

    public void saveOnlineSession(Player player) {
        stop(player);
        start(player);
    }

    public long getPlaytime(UUID playerId) {
        long savedPlaytime = repository.getPlaytime(playerId);
        Long sessionStart = sessionStarts.get(playerId);

        if (sessionStart == null) {
            return savedPlaytime;
        }

        return savedPlaytime + (System.currentTimeMillis() - sessionStart);
    }

    public String format(long milliseconds) {
        long totalMinutes = milliseconds / 60_000L;

        long days = totalMinutes / 1_440L;
        long hours = (totalMinutes % 1_440L) / 60L;
        long minutes = totalMinutes % 60L;

        if (days > 0) {
            return days + "d " + hours + "h";
        }

        if (hours > 0) {
            return hours + "h " + minutes + "min";
        }

        return minutes + "min";
    }
}