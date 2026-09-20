package de.lifemytouch.ansi.playtime.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.playtime.PlaytimeService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaytimeCommand implements CommandExecutor {

    private final PlaytimeService playtimeService;

    public PlaytimeCommand(PlaytimeService playtimeService) {
        this.playtimeService = playtimeService;
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

        OfflinePlayer target = player;

        if (args.length == 1) {
            target = Bukkit.getOfflinePlayer(args[0]);

            if (!target.hasPlayedBefore() && !target.isOnline()) {
                player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
                return true;
            }
        }

        if (args.length > 1) {
            player.sendMessage(
                    Messages.getPREFIX()
                            + "§7Nutze: §6/playtime [Spieler]"
            );
            return true;
        }

        String name = target.getName() == null
                ? target.getUniqueId().toString().substring(0, 8)
                : target.getName();

        player.sendMessage(
                Messages.getPREFIX()
                        + "§7Spielzeit von §6"
                        + name
                        + "§7: §b"
                        + playtimeService.format(
                        playtimeService.getPlaytime(
                                target.getUniqueId()
                        )
                )
        );

        return true;
    }
}