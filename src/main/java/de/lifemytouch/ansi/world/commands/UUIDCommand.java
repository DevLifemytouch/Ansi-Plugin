package de.lifemytouch.ansi.world.commands;

import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.Buffer;
import java.util.UUID;

public class UUIDCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(!player.hasPermission("ansi.commands.uuid")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        if(args.length == 0) {
            sendHelp(player);
            return true;
        }

        if(args.length == 1) {
            handleUUID(player, args);
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(Messages.getPREFIX() + "§6§lUUID");
        player.sendMessage(Messages.getPREFIX() + "§7/uuid <Spieler>");
    }

    private void handleUUID(Player player, String[] args) {


        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        UUID offlinePlayerUUID = offlinePlayer.getUniqueId();

        player.sendMessage(Messages.getPREFIX() + "§7Die UUID von §6" + offlinePlayer.getName() + "§7 ist:");
        player.sendMessage(Messages.getPREFIX() + "§7" + offlinePlayerUUID);

    }
}
