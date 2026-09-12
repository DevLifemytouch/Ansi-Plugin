package de.lifemytouch.ansi.punish.completer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BanTabCompleter implements TabCompleter {

    private static final List<String> DURATIONS = Arrays.asList("30m", "1h", "3h", "1d");
    private static final List<String> REASONS = Arrays.asList("HACKING", "CHAT", "BUGUSING", "ADVERTISING", "OTHER");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 1) {
            String input = args[0].toLowerCase();

            return Arrays.stream(Bukkit.getOfflinePlayers())
                    .map(OfflinePlayer::getName)
                    .filter(Objects::nonNull)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }

        if(args.length == 2) {
            return DURATIONS.stream()
                    .filter(duration -> duration.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if(args.length == 3) {
            return REASONS.stream()
                    .filter(reason -> reason.startsWith(args[2].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
