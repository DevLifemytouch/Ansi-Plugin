package de.lifemytouch.ansi.server.scoreboard;

import de.lifemytouch.ansi.coin.CoinService;
import de.lifemytouch.ansi.rank.Rank;
import de.lifemytouch.ansi.rank.RankManager;
import de.lifemytouch.ansi.server.tab.TabListManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {

    private final RankManager rankManager;
    private final CoinService coinService;

    public ScoreboardManager(
            RankManager rankManager,
            CoinService coinService
    ) {
        this.rankManager = rankManager;
        this.coinService = coinService;
    }

    public void update(Player player) {

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        player.setScoreboard(scoreboard);

        updateTabList(player);
        updateScoreboard(player);
    }

    public void updateTabList(Player viewer) {

        Scoreboard scoreboard = viewer.getScoreboard();

        clearTeams(scoreboard);

        for (Player online : Bukkit.getOnlinePlayers()) {

            Rank rank = rankManager.getRank(online);

            Team team = scoreboard.getTeam(rank.getTeamName());

            if (team == null) {
                team = scoreboard.registerNewTeam(rank.getTeamName());
            }

            team.setPrefix(rank.getPrefix());
            team.setColor(ChatColor.GRAY);

            team.addEntry(online.getName());
        }

        TabListManager.update(viewer);
    }

    public void updateTabListForAll() {

        for (Player online : Bukkit.getOnlinePlayers()) {
            updateTabList(online);
        }
    }

    private void clearTeams(Scoreboard scoreboard) {

        for (Team team : scoreboard.getTeams()) {
            team.unregister();
        }
    }

    public void updateScoreboard(Player player) {

        Scoreboard scoreboard = player.getScoreboard();

        Objective oldObjective = scoreboard.getObjective("ansi");

        if (oldObjective != null) {
            oldObjective.unregister();
        }

        Objective objective = scoreboard.registerNewObjective(
                "ansi",
                "dummy",
                "§6§lANSI"
        );

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Rank rank = rankManager.getRank(player);
        long coins = coinService.getCoins(player.getUniqueId());

        objective.getScore("§8").setScore(8);

        objective.getScore("§7Rang: ").setScore(7);
        objective.getScore(getScoreboardRank(rank)).setScore(6);

        objective.getScore("§5").setScore(5);

        objective.getScore("§7Coins:").setScore(4);
        objective.getScore("§e" + coins).setScore(3);

        objective.getScore("§2").setScore(2);

        objective.getScore(
                "§7Online: §a"
                        + Bukkit.getOnlinePlayers().size()
                        + "§7/§a"
                        + Bukkit.getMaxPlayers()
        ).setScore(1);

        objective.getScore("§0").setScore(0);
    }

    public void updateScoreboardForAll() {

        for (Player online : Bukkit.getOnlinePlayers()) {
            updateScoreboard(online);
        }
    }

    private String getScoreboardRank(Rank rank) {

        return switch (rank) {
            case OWNER -> "§4§lOwner";
            case DEV -> "§3§lDeveloper";
            case ADMIN -> "§c§lAdministrator";
            case MOD -> "§2§lModerator";
            case TESTER -> "§b§lTester";
            case BUILDER -> "§a§lBuilder";
            case MEDIA -> "§5§lMedia";
            case VIPP -> "§d§lVIP+";
            case VIP -> "§d§lVIP";
            case PREM -> "§e§lPremium";
            case DEFAULT -> "§7§lSpieler";
        };
    }
}
