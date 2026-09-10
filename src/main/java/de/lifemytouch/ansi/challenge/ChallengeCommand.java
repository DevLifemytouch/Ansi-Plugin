package de.lifemytouch.ansi.challenge;

import de.lifemytouch.ansi.Ansi;
import de.lifemytouch.ansi.challenge.gui.ChallengeGUI;
import de.lifemytouch.ansi.challenge.setting.ChallengeSettingGUI;
import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChallengeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return false;

        if(!player.hasPermission("ansi.commands.challenge")) {
            player.sendMessage(Messages.getNO_PERMS());
            return false;
        }

        if (args.length == 0) {
            openChallengeGUI(player);
            return true;
        }

        if(args[0].equalsIgnoreCase("settings")) {
            ChallengeSettingGUI.open(player);
            return true;
        }

        return true;
    }

    private void openChallengeGUI(Player player) {
        ChallengeGUI.open(player);
        player.sendMessage(Messages.getPREFIX() + "§7Wähle eine Challenge aus!");
    }
}
