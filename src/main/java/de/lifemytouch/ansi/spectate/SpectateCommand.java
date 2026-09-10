package de.lifemytouch.ansi.spectate;

import de.lifemytouch.ansi.Ansi;
import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return false;

        if(!player.hasPermission("ansi.command.spectate")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        if(args.length == 0) {

        }

        return false;
    }
}
