package de.lifemytouch.ansi.vanish;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.fly.FlyService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    private VanishService vanishService;
    private FlyService flyService;

    public VanishCommand(FlyService flyService, VanishService vanishService) {
        this.vanishService = vanishService;
        this.flyService = flyService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(args.length != 0) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /vanish");
            return true;
        }

        if(vanishService.isVanished(player)) {
            vanishService.setVanish(player, false);
            flyService.setFly(player, false);
            player.sendMessage(Messages.getPREFIX() + "§7Du bist nicht mehr im Vanish!");
        } else {
            vanishService.setVanish(player, true);
            flyService.setFly(player, true);
            player.sendMessage(Messages.getPREFIX() + "§7Du bist nun im Vanish!");
        }

        return false;
    }
}
