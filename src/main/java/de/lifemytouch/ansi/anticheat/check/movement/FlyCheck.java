package de.lifemytouch.ansi.anticheat.check.movement;

import de.lifemytouch.ansi.anticheat.AntiCheatManager;
import de.lifemytouch.ansi.anticheat.check.Check;
import org.bukkit.event.Listener;

public class FlyCheck extends Check implements Listener {

    public FlyCheck(AntiCheatManager antiCheatManager) {
        super(antiCheatManager);
    }

    @Override
    public String getName() {
        return "Fly";
    }

}
