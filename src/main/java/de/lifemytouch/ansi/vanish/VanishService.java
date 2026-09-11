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

    public void setVanish(Player player, boolean vanished) {

        if(vanished) {

            if(vanishList.contains(player)) return;

            for(Player players : Bukkit.getOnlinePlayers()) {
                players.hidePlayer(javaPlugin, player);
            }

            vanishList.add(player);

            return;
        }

        if(!vanishList.contains(player)) return;

        for(Player players : Bukkit.getOnlinePlayers()) {
            players.showPlayer(javaPlugin, player);
        }

        vanishList.remove(player);
    }

    public boolean isVanished(Player player) {
        return vanishList.contains(player);
    }

}
