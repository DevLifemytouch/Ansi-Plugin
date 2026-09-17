package de.lifemytouch.ansi.coin.completer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CoinsCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList(
            "set",
            "add",
            "remove",
            "hide"
    );

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (args.length == 1) {

            String input = args[0].toLowerCase();

            List<String> result = SUBCOMMANDS.stream()
                    .filter(sub -> sub.startsWith(input))
                    .collect(Collectors.toList());

            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.getName().toLowerCase().startsWith(input)) {
                    result.add(player.getName());
                }
            });

            return result;
        }

        if (args.length == 2 &&
                (args[0].equalsIgnoreCase("set")
                        || args[0].equalsIgnoreCase("add")
                        || args[0].equalsIgnoreCase("remove"))) {

            String input = args[1].toLowerCase();

            return Arrays.stream(Bukkit.getOfflinePlayers())
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}