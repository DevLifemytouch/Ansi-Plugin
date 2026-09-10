package de.lifemytouch.ansi.challenge.setting;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class ChallengeSettingManager {

    private boolean hardcore = false;
    private boolean blockRandomizer = false;
    private JavaPlugin javaPlugin;

    public ChallengeSettingManager (JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public boolean isHardcore() {
        return hardcore;
    }

    public void setHardcore(World world, boolean hardcore) {
        this.hardcore = hardcore;

        if(world != null) {
            world.setHardcore(hardcore);
        }
    }

    public void toggleHardcore(World world) {
        setHardcore(world, !hardcore);
    }

    public boolean isBlockRandomizer() {
        return blockRandomizer;
    }

    public void setBlockRandomizer(boolean blockRandomizer) {
        this.blockRandomizer = blockRandomizer;
    }

    public void toggleBlockRandomizer() {
        this.blockRandomizer = !this.blockRandomizer;
    }

}
