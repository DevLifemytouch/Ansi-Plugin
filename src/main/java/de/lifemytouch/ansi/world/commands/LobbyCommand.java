package de.lifemytouch.ansi.world.commands;

import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return false;

        if(!player.hasPermission("ansi.commands.lobby")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        if(args.length != 0) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /lobby");
            return true;
        }

        player.teleport(new Location(player.getWorld(), -1487.5, 35, 854.5, 0,0));
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        player.sendMessage(Messages.getPREFIX() + "§7Du wurdest zum Spawn teleportiert!");

        return false;
    }
}
