package de.lifemytouch.ansi.challenge.tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChallengeTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("settings");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if(args.length == 1) {
            return SUBCOMMANDS.stream().filter(sub -> sub.startsWith(args[0]
                    .toLowerCase()))
                    .collect(Collectors.toList());
        }

        return completions;
    }
}
