package de.lifemytouch.ansi.report.commands;

import de.lifemytouch.ansi.core.text.Messages;
import de.lifemytouch.ansi.report.ReportFilter;
import de.lifemytouch.ansi.report.ReportService;
import de.lifemytouch.ansi.report.gui.ReportListGUI;
import de.lifemytouch.ansi.report.gui.ReportListHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportsCommand implements CommandExecutor {

    private final ReportService reportService;

    public ReportsCommand(ReportService reportService ) {
        this.reportService = reportService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        if(!player.hasPermission("ansi.reports.handle")) {
            player.sendMessage(Messages.getNO_PERMS());
            return true;
        }

        ReportListGUI.open(player, reportService, 0, ReportFilter.OPEN);

        return true;
    }
}
