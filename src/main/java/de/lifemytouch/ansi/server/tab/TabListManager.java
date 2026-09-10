package de.lifemytouch.ansi.server.tab;

import de.lifemytouch.ansi.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TabListManager {

    public static void updatePrefix(Player player) {
        Scoreboard scoreboard = player.getServer().getScoreboardManager().getMainScoreboard();

        Rank rank = Rank.getHighest(player);

        Team team = scoreboard.getTeam(rank.getTeamName());
        if (team == null) {
            team = scoreboard.registerNewTeam(rank.getTeamName());
        }

        team.setPrefix(rank.getPrefix());
        team.setColor(ChatColor.GRAY);
        team.addEntry(player.getName());
    }

}
