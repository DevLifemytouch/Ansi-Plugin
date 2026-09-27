package de.lifemytouch.ansi.anticheat.check.combat;

import de.lifemytouch.ansi.anticheat.AntiCheatManager;
import de.lifemytouch.ansi.anticheat.check.Check;

public class ReachCheck extends Check {

    public ReachCheck(AntiCheatManager antiCheatManager) {
        super(antiCheatManager);
    }

    @Override
    public String getName() {
        return "Reach";
    }
}
