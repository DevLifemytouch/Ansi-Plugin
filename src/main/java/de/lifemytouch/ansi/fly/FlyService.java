package de.lifemytouch.ansi.fly;

import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyService {

    private static List<Player> flyList = new ArrayList<>();

    public static void handleFly(Player player) {

        if(flyList.contains(player)) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage(Messages.getPREFIX() + "§7Der Flugmodus wurde deaktiviert!");
            flyList.remove(player);
        } else {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage(Messages.getPREFIX() + "§7Der Flugmodus wurde aktiviert!");
            flyList.add(player);
        }

    }

}
