package de.lifemytouch.ansi.anticheat;

import de.lifemytouch.ansi.anticheat.check.Check;
import de.lifemytouch.ansi.anticheat.check.combat.AutoClickerCheck;
import de.lifemytouch.ansi.anticheat.check.combat.KillAuraCheck;
import de.lifemytouch.ansi.anticheat.check.combat.ReachCheck;
import de.lifemytouch.ansi.anticheat.check.movement.FlyCheck;
import de.lifemytouch.ansi.anticheat.check.movement.NoFallCheck;
import de.lifemytouch.ansi.anticheat.check.movement.SpeedCheck;
import de.lifemytouch.ansi.anticheat.violation.ViolationManager;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class AntiCheatManager {

    private final ViolationManager violationManager;
    private final AntiCheatAlertService alertService;
    private final Map<String, Check> checks = new LinkedHashMap<>();

    public AntiCheatManager(JavaPlugin javaPlugin, ReportService reportService) {
        this.alertService = new AntiCheatAlertService();
        this.violationManager = new ViolationManager(reportService, alertService);

        registerChecks(javaPlugin);
    }

    private void registerChecks(JavaPlugin javaPlugin) {

        SpeedCheck speedCheck = new SpeedCheck(this);
        register(speedCheck);

        NoFallCheck noFallCheck = new NoFallCheck(this);
        register(noFallCheck);

        FlyCheck flyCheck = new FlyCheck(this);
        register(flyCheck);

        // javaPlugin.getServer().getPluginManager().registerEvents(speedCheck, javaPlugin);
        javaPlugin.getServer().getPluginManager().registerEvents(noFallCheck, javaPlugin);
        javaPlugin.getServer().getPluginManager().registerEvents(flyCheck, javaPlugin);

        register(new KillAuraCheck(this));
        register(new ReachCheck(this));
        register(new AutoClickerCheck(this));
    }

    private void register(Check check) {
        checks.put(check.getName().toLowerCase(), check);
    }

    public Check getCheck(String name) {
        if (name == null) {
            return null;
        }

        return checks.get(
                name.toLowerCase()
        );
    }

    public Collection<Check> getChecks() {
        return checks.values();
    }

    public ViolationManager getViolationManager() {
        return violationManager;
    }

    public AntiCheatAlertService getAlertService() {
        return alertService;
    }

}
