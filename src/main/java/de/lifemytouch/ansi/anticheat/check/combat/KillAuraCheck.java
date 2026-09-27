package de.lifemytouch.ansi.anticheat.check.combat;

import de.lifemytouch.ansi.anticheat.AntiCheatManager;
import de.lifemytouch.ansi.anticheat.check.Check;

public class KillAuraCheck extends Check {

    public KillAuraCheck(AntiCheatManager antiCheatManager) {
        super(antiCheatManager);
    }

    @Override
    public String getName() {
        return "Kill Aura";
    }

}
