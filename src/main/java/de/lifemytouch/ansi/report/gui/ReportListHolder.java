package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.report.ReportFilter;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ReportListHolder implements InventoryHolder {

    private final ReportService reportService;
    private final int page;
    private final ReportFilter reportFilter;

    public ReportListHolder(
            ReportService reportService,
            int page,
            ReportFilter reportFilter
    ) {
        this.reportService = reportService;
        this.page = page;
        this.reportFilter = reportFilter;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public int getPage() {
        return page;
    }

    public ReportFilter getReportFilter() {
        return reportFilter;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
