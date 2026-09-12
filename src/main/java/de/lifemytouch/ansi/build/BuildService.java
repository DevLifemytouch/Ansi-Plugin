package de.lifemytouch.ansi.build;

import de.lifemytouch.ansi.player.listener.PlayerJoinListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BuildService {

    public List<Player> buildList = new ArrayList<>();
    private JavaPlugin javaPlugin;

    public BuildService(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void setBuildMode(Player player, boolean buildMode) {
        if (!buildMode) {
            buildList.remove(player);
        } else {
            buildList.add(player);
        }
    }

}
