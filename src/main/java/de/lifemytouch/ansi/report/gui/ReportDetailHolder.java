package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ReportDetailHolder implements InventoryHolder {

    private final long reportId;
    private final ReportService reportService;

    public ReportDetailHolder(
            long reportId,
            ReportService reportService
    ) {
        this.reportId = reportId;
        this.reportService = reportService;
    }

    public long getReportId() {
        return reportId;
    }

    public ReportService getReportService() {
        return reportService;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
