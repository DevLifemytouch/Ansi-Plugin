package de.lifemytouch.ansi.fly;

import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyService {

    private List<Player> flyList = new ArrayList<>();

    public void setFly(Player player, boolean flying) {

        if (flying) {

            if(flyList.contains(player)) return;

            player.setAllowFlight(true);
            player.setFlying(true);
            flyList.add(player);

            return;
        }

        if(!flyList.contains(player)) return;

        player.setFlying(false);
        player.setAllowFlight(false);
        flyList.remove(player);

    }

    public boolean getPlayerInList(Player player) {
        return flyList.contains(player);
    }

}
