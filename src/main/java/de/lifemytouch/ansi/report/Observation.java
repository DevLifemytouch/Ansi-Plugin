package de.lifemytouch.ansi.report;

import org.bukkit.Location;

import java.util.UUID;

public record Observation(
        UUID target,
        Location returnLocation,
        boolean wasAlreadyVanished
) {
}