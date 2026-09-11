package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.report.ReportFilter;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ReportDetailHolder implements InventoryHolder {

    private final long reportId;
    private final ReportService reportService;
    private final ReportFilter reportFilter;

    public ReportDetailHolder(
            long reportId,
            ReportService reportService,
            ReportFilter reportFilter
    ) {
        this.reportId = reportId;
        this.reportService = reportService;
        this.reportFilter = reportFilter;
    }

    public long getReportId() {
        return reportId;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public ReportFilter getReportFilter() {
        return reportFilter;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
