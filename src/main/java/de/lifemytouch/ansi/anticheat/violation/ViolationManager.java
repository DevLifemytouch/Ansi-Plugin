package de.lifemytouch.ansi.anticheat.violation;

import de.lifemytouch.ansi.anticheat.AntiCheatAlertService;
import de.lifemytouch.ansi.report.ReportCategory;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ViolationManager {

    private static final double AUTO_REPORT_THRESHOLD = 20.0;
    private final Map<UUID, PlayerViolationData> players = new ConcurrentHashMap<>();
    private final ReportService reportService;
    private final AntiCheatAlertService alertService;

    public ViolationManager(ReportService reportService, AntiCheatAlertService alertService) {
        this.reportService = reportService;
        this.alertService = alertService;
    }

    public void flag(Player player, String check, double amount, String information) {
        if(player == null) return;
        if(check == null || check.isBlank()) return;
        if(amount <= 0) return;

        PlayerViolationData playerViolationData = players.computeIfAbsent(
                player.getUniqueId(), PlayerViolationData::new);

        Violation violation = new Violation(
                player.getUniqueId(), check, amount, information, System.currentTimeMillis());

        playerViolationData.addViolation(violation);
        alertService.sendAlert(player, violation, playerViolationData.getTotalViolations());

        if(playerViolationData.getTotalViolations() >= AUTO_REPORT_THRESHOLD && !playerViolationData.isAutoReported()) {
            createAutoReport(player, playerViolationData);
        }
    }

    private void createAutoReport(Player player, PlayerViolationData playerViolationData) {
        playerViolationData.setAutoReported(true);

        reportService.createSystemReport(player, ReportCategory.HACKING, createReportReason(playerViolationData));
    }

    private String createReportReason(PlayerViolationData playerViolationData) {
        StringBuilder reason = new StringBuilder();

        reason.append("§7AntiCheat Detection\n");
        reason.append("§7Total Flags: ").append(playerViolationData.getTotalViolations()).append("\n\n");

        playerViolationData.getCheckViolations().forEach((check, violations) -> {
            reason.append(check).append(": ").append(violations).append("\n");
        });

        return reason.toString();
    }

    public PlayerViolationData getData(Player player) {
        return player == null
                ? null
                : getData(player.getUniqueId());
    }

    public PlayerViolationData getData(UUID uuid) {
        return players.get(uuid);
    }

    public double getViolations(Player player) {
        PlayerViolationData data = getData(player);

        return data == null
                ? 0.0
                : data.getTotalViolations();
    }

    public void reset(Player player) {
        if (player == null) {
            return;
        }

        players.remove(player.getUniqueId());
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
    }

}
