package de.lifemytouch.ansi.maintenance.completer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        if (args.length == 1) {

            List<String> completions = new ArrayList<>();

            completions.add("add");
            completions.add("remove");
            completions.add("list");
            completions.add("toggle");

            return completions.stream()
                    .filter(value ->
                            value.toLowerCase()
                                    .startsWith(args[0].toLowerCase())
                    )
                    .toList();
        }

        if (args.length == 2
                && (args[0].equalsIgnoreCase("add")
                || args[0].equalsIgnoreCase("remove"))) {

            return Bukkit.getOnlinePlayers()
                    .stream()
                    .map(player -> player.getName())
                    .filter(name ->
                            name.toLowerCase()
                                    .startsWith(args[1].toLowerCase())
                    )
                    .toList();
        }

        return List.of();
    }
}