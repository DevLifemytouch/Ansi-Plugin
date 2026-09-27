package de.lifemytouch.ansi.anticheat;

import de.lifemytouch.ansi.anticheat.violation.Violation;
import de.lifemytouch.ansi.core.text.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AntiCheatAlertService {

    private static final String ALERT_PERMISSION = "ansi.anticheat.alerts";
    private final Map<UUID, Boolean> alerts = new ConcurrentHashMap<>();

    public boolean hasAlertsEnabled(Player player) {
        return alerts.getOrDefault(player.getUniqueId(), true);
    }

    public void toggle(Player player) {
        UUID uuid = player.getUniqueId();

        boolean enabled = !hasAlertsEnabled(player);

        alerts.put(uuid, enabled);
    }

    public void remove(Player player) {
        if(player == null) return;

        alerts.remove(player.getUniqueId());
    }

    public void sendAlert(Player target, Violation violation, double totalViolations) {
        String message =  Messages.getANTICHEAT_PREFIX() + "§c"
                + target.getName() + " §7flagged "
                + "§c" + violation.check()
                + " §8(§e" + violation.amount()
                + " §7VL§8) " + "§7Total: §c"
                + totalViolations  + "\n" + "§8» §7" + violation.information();

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!player.hasPermission(ALERT_PERMISSION)) continue;
            if(!hasAlertsEnabled(player)) continue;

            player.sendMessage(message);
        }
    }

}
