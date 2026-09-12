package de.lifemytouch.ansi.punish.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.punish.DurationParser;
import de.lifemytouch.ansi.punish.Punishment;
import de.lifemytouch.ansi.punish.PunishmentCategory;
import de.lifemytouch.ansi.punish.PunishmentService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;

public class MuteCommand implements CommandExecutor {

    private final PunishmentService punishmentService;

    public MuteCommand(PunishmentService punishmentService) {
        this.punishmentService = punishmentService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(args.length < 3) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /mute <Spieler> <Dauer> <Grund>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if(target == null) {
            target.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return true;
        }

        Duration duration;

        try {

            duration = DurationParser.parse(args[1]);

        } catch (IllegalArgumentException exception) {
            player.sendMessage(Messages.getPREFIX() + "§7Ungültige Dauer!");
            return true;
        }

        String reason = String.join(
                " ", Arrays.copyOfRange(args, 2, args.length)
        );

        punishmentService.mute(target.getUniqueId(), player.getUniqueId(), PunishmentCategory.OTHER, reason, duration);

        player.sendMessage(Messages.getPREFIX() + "§7Du hast §6" + target.getName() + "§7 gemuted!");
        target.sendMessage(Messages.getPREFIX() + "§cDu wurdest gemuted!\n"
                + "§7Grund: §6" + reason);

        return true;
    }
}
