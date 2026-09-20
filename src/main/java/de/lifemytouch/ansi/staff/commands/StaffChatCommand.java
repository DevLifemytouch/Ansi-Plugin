package de.lifemytouch.ansi.staff.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.staff.StaffChatService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Member;

public class StaffChatCommand implements CommandExecutor {

    private final StaffChatService staffChatService;

    public StaffChatCommand(StaffChatService staffChatService) {
        this.staffChatService = staffChatService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        if(!player.hasPermission("ansi.commands.sc")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        boolean enabled = staffChatService.toggle(player.getUniqueId());

        player.sendMessage(
                Messages.getPREFIX() +
                        (enabled
                                ? "§7Staff-Chat §aaktiviert§7."
                                : "§7Staff-Chat §cdeaktiviert§7.")
        );

        return true;
    }
}
