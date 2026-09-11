package de.lifemytouch.ansi.fly;

import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(args.length != 0) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /fly");
            return true;
        }

        FlyService.handleFly(player);

        return true;
    }
}
