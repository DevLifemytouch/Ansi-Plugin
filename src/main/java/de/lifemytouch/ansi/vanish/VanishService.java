package de.lifemytouch.ansi.vanish;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.fly.FlyService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class VanishService {

    private List<Player> vanishList = new ArrayList<>();
    private JavaPlugin javaPlugin;

    public VanishService(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void handleVanish(Player player) {

        if(vanishList.contains(player)) {
            for(Player players : Bukkit.getOnlinePlayers()) {
                players.showPlayer(javaPlugin, player);
            }
            FlyService.handleFly(player);
            player.sendMessage(Messages.getPREFIX() + "§7Du bist nicht mehr im Vanish!");

            vanishList.remove(player);

            return;
        } else {
            for(Player players : Bukkit.getOnlinePlayers()) {
                players.hidePlayer(javaPlugin, player);
            }
            FlyService.handleFly(player);
            player.sendMessage(Messages.getPREFIX() + "§7Du bist nun im Vanish!");

            vanishList.add(player);

            return;
        }

    }

}
