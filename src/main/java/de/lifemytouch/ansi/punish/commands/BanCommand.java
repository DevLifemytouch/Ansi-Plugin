package de.lifemytouch.ansi.punish.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.punish.DurationParser;
import de.lifemytouch.ansi.punish.PunishmentCategory;
import de.lifemytouch.ansi.punish.PunishmentService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.Buffer;
import java.time.Duration;
import java.util.Arrays;

public class BanCommand implements CommandExecutor {

    private final PunishmentService punishmentService;

    public BanCommand(PunishmentService punishmentService) {
        this.punishmentService = punishmentService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(!player.hasPermission("ansi.punish.ban")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        if(args.length < 3) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /ban <Spieler> <Dauer> <Grund>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        Duration duration;

        try {
            duration = DurationParser.parse(args[1]);
        } catch (IllegalArgumentException exception) {
            player.sendMessage(Messages.getPREFIX() + "§7Ungültige Dauer!");
            return true;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        punishmentService.ban(target.getUniqueId(), player.getUniqueId(), PunishmentCategory.OTHER, reason, duration);

        if(target.isOnline()) {
            Player onlineTarget = target.getPlayer();

            if(onlineTarget != null) {
                onlineTarget.kickPlayer(
                        "§cDu wurdest vom Server gebannt!\n\n" + "§7Grund: §6" + reason
                );
            }
        }


        player.sendMessage(Messages.getPREFIX() + "§7Du hast §6" + target.getName() + "§7 gebannt!");
        player.sendMessage(Messages.getPREFIX() + "§7Grund: §6" + reason);

        return false;
    }
}
