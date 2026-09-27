package de.lifemytouch.ansi.anticheat.check;

import de.lifemytouch.ansi.anticheat.AntiCheatManager;
import org.bukkit.entity.Player;

public abstract class Check {
    protected final AntiCheatManager antiCheatManager;
    private boolean enabled = true;

    protected Check(AntiCheatManager antiCheatManager) {
        this.antiCheatManager = antiCheatManager;
    }

    public abstract String getName();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected void flag(Player player, double amount, String information) {
        if(!enabled) return;
        antiCheatManager.getViolationManager().flag(player, getName(), amount, information);
    }
}
