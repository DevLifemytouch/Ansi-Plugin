package de.lifemytouch.ansi.server.tab;

import org.bukkit.entity.Player;

public class TabListManager {

    public static void update(Player player) {

        updateHeaderFooter(player);
    }

    private static void updateHeaderFooter(Player player) {

        player.setPlayerListHeader(
                "§6§lANSI\n" +
                "§7Willkommen auf dem Server!\n");

        player.setPlayerListFooter(
                "\n"
                        + "§7Online: §a"
                        + player.getServer().getOnlinePlayers().size()
                        + "§7/§a"
                        + player.getServer().getMaxPlayers()
                        + "\n\n"
                        + "§7play.ansi.de"
        );
    }
}
