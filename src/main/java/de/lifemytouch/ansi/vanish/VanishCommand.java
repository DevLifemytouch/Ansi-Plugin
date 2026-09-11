package de.lifemytouch.ansi.vanish;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.fly.FlyService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    private VanishService vanishService;

    public VanishCommand(VanishService vanishService) {
        this.vanishService = vanishService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(args.length != 0) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /vanish");
            return true;
        }

        if(vanishService.isVanished(player)) {
            vanishService.setVanish(player, true);
            FlyService.handleFly(player);
            player.sendMessage(Messages.getPREFIX() + "§7Du bist nun im Vanish!");
        } else {
            vanishService.setVanish(player, false);
            FlyService.handleFly(player);
            player.sendMessage(Messages.getPREFIX() + "§7Du bist nicht mehr im Vanish!");
        }

        return false;
    }
}
