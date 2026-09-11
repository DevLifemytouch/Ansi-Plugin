package de.lifemytouch.ansi.rank;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RankCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("set", "info");
    private static final List<String> RANKS = Arrays.asList("owner", "dev", "admin", "mod",
            "media", "vipp", "vip", "prem", "default");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 1) {
            return SUBCOMMANDS.stream()
                    .filter(sub -> sub.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if(args.length == 2 && (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("info"))) {
            String input = args[1].toLowerCase();

            return Arrays.stream(Bukkit.getOfflinePlayers())
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }

        if(args.length == 3 && args[0].equalsIgnoreCase("set")) {
            return RANKS.stream()
                    .filter(rang -> rang.startsWith(args[2].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
