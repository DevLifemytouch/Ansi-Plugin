package de.lifemytouch.ansi.rank;

import de.lifemytouch.ansi.Ansi;
import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {

    private final RankManager rankManager;

    public RankCommand(RankManager rankManager) {
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return false;

        if(!player.hasPermission("ansi.commands.rank")) {
            player.sendMessage(Messages.getNO_PERMS());
            return false;
        }

        if(args.length == 0) {
            player.sendMessage(Messages.getPREFIX() + "§7Dieser Command existiert nicht!");
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "set":
                return handleSet(player, args);
            case "info":
                return handleInfo(player, args);
            default:
                player.sendMessage(Messages.getPREFIX() + "§7/rang set <spieler> <rang>");
                player.sendMessage(Messages.getPREFIX() + "§7/rang info §8<spieler>");
                break;
        }

        return false;
    }

    private boolean handleSet(Player player, String[] args) {
        if(args.length != 3) {
            player.sendMessage(Messages.getPREFIX() + "§7/rang set <spieler> <rang>");
            return false;
        }

        Rank rank = Rank.fromName(args[2]);
        if(rank == null) {
            player.sendMessage(Messages.getPREFIX() + "§7Dieser Rang existiert nicht. Nutze: §fowner§7, §fdev§7, " +
                    "§fadmin§7, §fmod§7, §ftester§7, §fbuilder§7, §fmedia§7, §fvip_plus§7, §fvip§7, §fpremium§7, §fdefault§7.");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        rankManager.setRank(target, rank);

        if(target.isOnline()) {
            Player onlineTarget = target.getPlayer();

            if(onlineTarget != null) {
                onlineTarget.kickPlayer("§cDu wurdest vom Server gekickt!\n\n" +
                        "§7Dein Rang hat sich verändert!\n\n" + "§7Bitte verbinde dich erneut!"
                );
            }
        }

        player.sendMessage(Messages.getPREFIX() + "§6§l" + target.getName() + "§7 hat nun den Rang §f" +
                rank.name() + "§7.");
        return true;
    }

    private boolean handleInfo(Player player, String[] args) {
        if(args.length != 2) {
            player.sendMessage(Messages.getPREFIX() + "§7/rang info §8<spieler>");
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        Rank rank = rankManager.getRank(target);

        player.sendMessage(Messages.getPREFIX() + "§6§l" + target.getName() + "§7 hat den Rang §f" + rank.name() + "§7.");
        return true;
    }
}
