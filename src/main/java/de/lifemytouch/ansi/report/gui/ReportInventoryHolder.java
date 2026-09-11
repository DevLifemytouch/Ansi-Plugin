package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.report.Report;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public class ReportInventoryHolder implements InventoryHolder {

    private final UUID target;
    private final ReportService reportService;

    public ReportInventoryHolder(UUID target, ReportService reportService) {
        this.target = target;
        this.reportService = reportService;
    }

    public UUID getTarget() {
        return target;
    }

    public ReportService reportService() {
        return reportService;
    }

    public ReportService getReportService() {
        return reportService;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
