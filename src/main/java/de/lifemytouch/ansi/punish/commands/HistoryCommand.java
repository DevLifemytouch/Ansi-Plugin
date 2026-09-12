package de.lifemytouch.ansi.punish.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.punish.DurationParser;
import de.lifemytouch.ansi.punish.Punishment;
import de.lifemytouch.ansi.punish.PunishmentService;
import de.lifemytouch.ansi.punish.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.plaf.basic.BasicButtonUI;
import java.lang.reflect.Member;
import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryCommand implements CommandExecutor {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final PunishmentService punishmentService;

    public HistoryCommand(PunishmentService punishmentService) {
        this.punishmentService = punishmentService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(args.length != 1) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /history <Spieler>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if(target == null) {
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return true;
        }

        List<Punishment> punishments = punishmentService.getPunishments(target.getUniqueId());


        player.sendMessage(Messages.getPREFIX() + "§8§l§m---------------------------------------------------");
        player.sendMessage(Messages.getPREFIX() + "§7Punishment-History von §6" + target.getName());
        player.sendMessage("");
        player.sendMessage("");

        if(punishments.isEmpty()) {
            player.sendMessage(Messages.getPREFIX() + "§7Keine Punishments vorhanden!");
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage(Messages.getPREFIX() + "§8§l§m---------------------------------------------------");
            return true;
        }

        for(Punishment punishment : punishments) {
            String type = getTypeName(punishment.getPunishmentType());

            String duration = getDuration(punishment);

            String status = punishment.isActive() ? "§aAktiv" : "§cAbgelaufen";

            player.sendMessage(type + " §8- §6" + status);
            player.sendMessage("§7Grund: §6" + punishment.getReason());
            player.sendMessage("§7Kategorie: §6" + punishment.getPunishmentCategory());
            player.sendMessage("§7Datum: §6" + DATE_FORMAT.format(punishment.getCreatedAt()
                    .atZone(ZoneId.systemDefault())));
            player.sendMessage("§7Dauer: §6" + duration);
            player.sendMessage("§8");
        }

        player.sendMessage(Messages.getPREFIX() + "§8§l§m---------------------------------------------------");

        return true;
    }

    private String getTypeName(PunishmentType punishmentType) {
        return switch (punishmentType) {
            case BAN -> "§4BAN";
            case KICK -> "§6KICK";
            case MUTE -> "§cMUTE";
        };
    }

    private String getDuration(Punishment punishment) {
        if(punishment.isPermanent()) {
            return "Permanent";
        }

        Duration duration = Duration.between(punishment.getCreatedAt(), punishment.getExpiresAt());

        return formatDuration(duration);
    }

    private String formatDuration(Duration duration) {

        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        if(days > 0) {
            return days + " Tage " +
                    hours + " Stunden " + minutes + " Minutes";
        }

        if(hours > 0) {
            return hours + " Stunden " +
                    minutes + " Minuten " + seconds + " Sekunden";
        }

        if(minutes > 0) {
            return minutes + " Minuten " + seconds + " Sekunden";
        }

        if(seconds > 0) {
            return seconds + " Sekunden";
        }

        return "Sofort";
    }

}
