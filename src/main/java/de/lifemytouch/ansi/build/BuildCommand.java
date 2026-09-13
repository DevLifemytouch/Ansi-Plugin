package de.lifemytouch.ansi.build;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.world.inventories.LobbyHotbar;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

    private final BuildService buildService;

    public BuildCommand(BuildService buildService) {
        this.buildService = buildService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(!player.hasPermission("ansi.commands.build")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        if(args.length != 0) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /build");
            return true;
        }

        if(buildService.buildList.contains(player)) {
            buildService.setBuildMode(player, false);
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            LobbyHotbar.setSpawnItems(player);
            player.sendMessage(Messages.getPREFIX() + "§7Du bist nun nicht mehr im Build-Modus!");
        } else {
            buildService.setBuildMode(player, true);
            player.setGameMode(GameMode.CREATIVE);
            player.getInventory().clear();
            player.sendMessage(Messages.getPREFIX() + "§7Du bist nun im Build-Modus!");
        }

        return true;
    }
}
