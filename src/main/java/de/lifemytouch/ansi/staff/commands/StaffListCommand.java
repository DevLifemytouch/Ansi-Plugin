package de.lifemytouch.ansi.staff;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.rank.Rank;
import de.lifemytouch.ansi.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StaffListCommand implements CommandExecutor {

    private final RankManager rankManager;

    public StaffListCommand(RankManager rankManager) {
        this.rankManager = rankManager;
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

        if (!player.hasPermission("ansi.commands.sc")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        List<Player> staffMembers = new ArrayList<>(Bukkit.getOnlinePlayers());

        staffMembers.removeIf(
                online -> !online.hasPermission("ansi.commands.sc")
        );

        staffMembers.sort((first, second) -> Integer.compare(
                rankManager.getRank(second).getWeight(),
                rankManager.getRank(first).getWeight()
        ));

        player.sendMessage(" ");
        player.sendMessage(Messages.getPREFIX() + "§6§lTeam");

        if (staffMembers.isEmpty()) {
            player.sendMessage(Messages.getPREFIX() + "§7Aktuell ist kein Teammitglied online.");
        } else {
            for (Player staffMember : staffMembers) {
                Rank rank = rankManager.getRank(staffMember);

                player.sendMessage("§8• " + rank.getPrefix() + staffMember.getName() + "§8» §aOnline");
            }
        }

        player.sendMessage(" ");
        return true;
    }
}