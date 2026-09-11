package de.lifemytouch.ansi.report.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.report.ReportService;
import de.lifemytouch.ansi.report.gui.ReportGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

    private final ReportService reportService;

    public ReportCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(args.length != 1) {
            player.sendMessage(Messages.getPREFIX() + "§7Nutze: /report <Spieler>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if(target == null) {
            player.sendMessage(Messages.getPLAYER_NOT_ONLINE());
            return true;
        }

        if(target.equals(player)) {
            player.sendMessage(Messages.getPREFIX() + "§7Du kannst dich nicht selber melden!");
            return true;
        }

        ReportGUI.open(player, target, reportService);

        return true;
    }
}
