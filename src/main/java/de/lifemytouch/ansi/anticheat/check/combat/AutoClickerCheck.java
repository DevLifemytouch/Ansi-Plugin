package de.lifemytouch.ansi.anticheat.check.combat;

import de.lifemytouch.ansi.anticheat.AntiCheatManager;
import de.lifemytouch.ansi.anticheat.check.Check;

public class AutoClickerCheck extends Check {
    public AutoClickerCheck(AntiCheatManager antiCheatManager) {
        super(antiCheatManager);
    }

    @Override
    public String getName() {
        return "Auto Clicker";
    }
}
