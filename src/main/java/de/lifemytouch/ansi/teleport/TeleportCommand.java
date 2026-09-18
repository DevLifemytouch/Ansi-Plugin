package de.lifemytouch.ansi.teleport;

import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(!player.hasPermission("ansi.commands.teleport")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        if(args.length == 0) {
            sendHelp(player);
            return true;
        }

        handleTeleport(player, args);

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(Messages.getPREFIX() + "§6§lTeleport");
        player.sendMessage(Messages.getPREFIX() + "§7/teleport <Spieler>");
        player.sendMessage(Messages.getPREFIX() + "§7/tp <Spieler>");
    }

    private void handleTeleport(Player player, String[] args) {
        if(args.length != 1) {
            sendHelp(player);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null) {
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return;
        }

        player.teleport(target);
        player.sendMessage(Messages.getPREFIX() + "§7Du wurdest zum Spieler §6" + target.getName() + "§7 teleportiert.");

    }
}
