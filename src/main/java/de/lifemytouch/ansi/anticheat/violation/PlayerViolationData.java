package de.lifemytouch.ansi.anticheat.violation;

import java.util.*;

public class PlayerViolationData {

    private final UUID playerId;
    private double totalViolations;
    private final Map<String, Double> checkViolations = new HashMap<>();
    private final List<Violation> history = new ArrayList<>();
    private boolean autoReported;

    public PlayerViolationData(UUID playerId) {
        this.playerId = playerId;
    }

    public void addViolation(Violation violation) {
        totalViolations += violation.amount();
        checkViolations.merge(violation.check(), violation.amount(), Double::sum);
        history.add(violation);
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public double getTotalViolations() {
        return totalViolations;
    }

    public Map<String, Double> getCheckViolations() {
        return Collections.unmodifiableMap(checkViolations);
    }

    public List<Violation> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public boolean isAutoReported() {
        return autoReported;
    }

    public void setAutoReported(boolean autoReported) {
        this.autoReported = autoReported;
    }
}
