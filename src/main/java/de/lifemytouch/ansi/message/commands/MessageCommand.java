package de.lifemytouch.ansi.message.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.message.PrivateMessageService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.lifemytouch.ansi.rank.Rank;

import java.util.Arrays;

public class MessageCommand implements CommandExecutor {

    private final PrivateMessageService privateMessageService;

    public MessageCommand(PrivateMessageService privateMessageService) {
        this.privateMessageService = privateMessageService;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(
                    Messages.getPREFIX()
                            + "§7Nutze: /msg <Spieler> <Nachricht>"
            );
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(
                    Messages.getPREFIX()
                            + "§cDu kannst dir nicht selbst schreiben."
            );
            return true;
        }

        if (!privateMessageService.canReceive(
                target.getUniqueId(),
                player.getUniqueId()
        )) {
            player.sendMessage(
                    Messages.getPREFIX()
                            + "§cDieser Spieler akzeptiert derzeit keine Nachrichten."
            );
            return true;
        }

        String message = String.join(
                " ",
                Arrays.copyOfRange(args, 1, args.length)
        );

        Rank senderRank = Rank.getHighest(player);
        Rank targetRank = Rank.getHighest(target);

        player.sendMessage(
                Messages.getPREFIX() +
                "§8[§6MSG§8] §7Du §8→ "
                        + targetRank.getPrefix()
                        + target.getName()
                        + "§8: §f" + message
        );

        target.sendMessage(
                Messages.getPREFIX() +
                "§8[§6MSG§8] "
                        + senderRank.getPrefix()
                        + player.getName()
                        + " §8→ §7Dir§8: §f" + message
        );

        return true;
    }
}