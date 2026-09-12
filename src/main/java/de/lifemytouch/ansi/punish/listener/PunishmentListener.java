package de.lifemytouch.ansi.punish.listener;

import de.lifemytouch.ansi.player.listener.PlayerJoinListener;
import de.lifemytouch.ansi.punish.Punishment;
import de.lifemytouch.ansi.punish.PunishmentService;
import de.lifemytouch.ansi.punish.PunishmentType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Duration;
import java.util.UUID;

public class PunishmentListener implements Listener {

    private final PunishmentService punishmentService;

    public PunishmentListener(PunishmentService punishmentService) {
        this.punishmentService = punishmentService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        UUID targetUUID = event.getPlayer().getUniqueId();

        Punishment punishment = punishmentService.getActivePunishment(targetUUID, PunishmentType.BAN);

        if(punishment == null) {
            return;
        }

        String reason = punishment.getReason();
        Duration remaining = punishment.getRemainingDuration();

        String durationText;

        if(punishment.isPermanent()) {
            durationText = "§4§lPERMANENT";
        } else {
            durationText = formatDuration(remaining);
        }

        event.getPlayer().kickPlayer(
                "§7Du bist vom Server §4§lGEBANNT!\n\n" +
                        "§7Grund: §6" + reason + "\n\n" +
                        "§7Dauer: §6" + durationText + "\n\n" +
                        "§7Stelle einen Entbannungsantrag unter: §6https://ansi.de/ea"
                );

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player target = event.getPlayer();
        UUID targetUUID = event.getPlayer().getUniqueId();

        Punishment punishment = punishmentService.getActivePunishment(targetUUID, PunishmentType.MUTE);

        if(punishment == null) return;

        event.setCancelled(true);

        Duration remaining = punishment.getRemainingDuration();

        String durationText;

        if(punishment.isPermanent()) {
            durationText = "§4§lPERMANENT";
        } else {
            durationText = formatDuration(remaining);
        }

        target.sendMessage(
                "\n\n\n§7Du bist §cstummgeschaltet§7!\n\n" +
                "§7Grund: §6" + punishment.getReason() + "\n" +
                "§7Dauer: §6" + durationText + "\n\n\n"
        );

    }

    private String formatDuration(Duration duration) {

        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        if(days > 0) {
            return days + " Tage " + hours + " Stunden " + minutes + " Minuten";
        }

        if(hours > 0) {
            return hours + " Stunden " + minutes + " Minuten " + seconds + " Seconds";
        }

        if(minutes > 0) {
            return minutes + " Minuten " + seconds + " Sekunden";
        }

        return seconds + " Sekunden";
    }

}
