package de.lifemytouch.ansi.report;

import java.util.UUID;

public record Observation(UUID target, boolean wasAlreadyVanished) {

}
