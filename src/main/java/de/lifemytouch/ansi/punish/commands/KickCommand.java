package de.lifemytouch.ansi.punish.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.punish.PunishmentCategory;
import de.lifemytouch.ansi.punish.PunishmentService;
import de.lifemytouch.ansi.punish.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.time.Duration;
import java.util.Arrays;

public class KickCommand implements CommandExecutor {

    private final PunishmentService punishmentService;

    public KickCommand(PunishmentService punishmentService) {
        this.punishmentService = punishmentService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(!player.hasPermission("ansi.punish.kick")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        if(args.length < 2) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /kick <Spieler> <Grund>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return true;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        punishmentService.punish(target.getUniqueId(),
                player.getUniqueId(), PunishmentType.KICK, PunishmentCategory.OTHER, reason,
                Duration.ZERO
        );

        target.kickPlayer("§cDu wurdest vom Server gekickt!\n\n" +
                "§7Grund: §6" + reason
        );

        player.sendMessage(Messages.getPREFIX() + "§7Du hast §6" + target.getName() + "§7 gekickt!");

        return true;
    }
}
