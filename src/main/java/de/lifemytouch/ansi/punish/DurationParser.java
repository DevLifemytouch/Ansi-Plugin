package de.lifemytouch.ansi.punish;

import java.time.Duration;

public class DurationParser {

    private DurationParser() {

    }

    public static Duration parse(String input) {
        if(input == null || input.isBlank()) return null;

        String value = input.toLowerCase();

        if(value.equals("perm")) return null;

        if(value.length() < 2) throw new IllegalArgumentException();

        String numberPart = value.substring(0, value.length() - 1);
        char unit = value.charAt(value.length() - 1);

        long amount;

        try {
            amount = Long.parseLong(numberPart);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException();
        }

        if(amount <= 0) throw new IllegalArgumentException();

        return switch (unit) {
            case 's' -> Duration.ofSeconds(amount);
            case 'm' -> Duration.ofMinutes(amount);
            case 'h' -> Duration.ofHours(amount);
            case 'd' -> Duration.ofDays(amount);
            default -> throw new IllegalArgumentException();
        };
    }

}
