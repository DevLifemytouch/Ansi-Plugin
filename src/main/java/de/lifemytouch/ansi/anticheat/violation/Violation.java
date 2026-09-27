package de.lifemytouch.ansi.anticheat.violation;

import java.util.UUID;

public record Violation(UUID playerId, String check, double amount, String information, long timestamp) {
}
