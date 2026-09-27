package de.lifemytouch.ansi.anticheat.completer;

import de.lifemytouch.ansi.anticheat.AntiCheatManager;
import de.lifemytouch.ansi.anticheat.check.Check;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AntiCheatTabCompleter implements TabCompleter {

    private final AntiCheatManager antiCheatManager;

    public AntiCheatTabCompleter(
            AntiCheatManager antiCheatManager
    ) {
        this.antiCheatManager = antiCheatManager;
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {
            return Collections.emptyList();
        }

        if (!player.hasPermission("ansi.anticheat")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {

            List<String> suggestions =
                    new ArrayList<>();

            suggestions.add("flags");
            suggestions.add("history");
            suggestions.add("check");

            return filter(
                    suggestions,
                    args[0]
            );
        }

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("flags")) {

                if (!player.hasPermission(
                        "ansi.anticheat.alerts"
                )) {
                    return Collections.emptyList();
                }

                return filter(
                        List.of("toggle"),
                        args[1]
                );
            }

            if (args[0].equalsIgnoreCase("history")) {

                if (!player.hasPermission(
                        "ansi.anticheat.history"
                )) {
                    return Collections.emptyList();
                }

                List<String> players =
                        new ArrayList<>();

                for (Player onlinePlayer :
                        player.getServer().getOnlinePlayers()) {

                    players.add(
                            onlinePlayer.getName()
                    );
                }

                return filter(
                        players,
                        args[1]
                );
            }

            if (args[0].equalsIgnoreCase("check")) {

                if (!player.hasPermission(
                        "ansi.anticheat.manage"
                )) {
                    return Collections.emptyList();
                }

                List<String> checks =
                        new ArrayList<>();

                for (Check check :
                        antiCheatManager.getChecks()) {

                    checks.add(
                            check.getName()
                    );
                }

                return filter(
                        checks,
                        args[1]
                );
            }
        }

        if (args.length == 3
                && args[0].equalsIgnoreCase("check")) {

            if (!player.hasPermission(
                    "ansi.anticheat.manage"
            )) {
                return Collections.emptyList();
            }

            return filter(
                    List.of("toggle"),
                    args[2]
            );
        }

        return Collections.emptyList();
    }

    private List<String> filter(
            List<String> values,
            String input
    ) {

        String lowerInput =
                input.toLowerCase();

        return values.stream()
                .filter(value ->
                        value.toLowerCase()
                                .startsWith(lowerInput)
                )
                .sorted()
                .toList();
    }
}