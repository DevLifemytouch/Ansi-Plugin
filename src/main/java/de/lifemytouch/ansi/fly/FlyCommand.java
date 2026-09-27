package de.lifemytouch.ansi.fly;

import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private FlyService flyService;

    public FlyCommand(FlyService flyService) {
        this.flyService = flyService;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(args.length != 0) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /fly");
            return true;
        }

        if (!flyService.getPlayerInList(player)) {
            flyService.setFly(player, true);
            player.sendMessage(Messages.getPREFIX() + "§7Der Flugmodus wurde §aaktiviert§7.");
            return true;
        }

        flyService.setFly(player, false);
        player.sendMessage(Messages.getPREFIX() + "§7Der Flugmodus wurde §cdeaktiviert§7.");

        return true;
    }
}
