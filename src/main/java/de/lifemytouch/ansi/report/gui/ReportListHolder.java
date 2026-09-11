package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ReportListHolder implements InventoryHolder {

    private final ReportService reportService;
    private final int page;

    public ReportListHolder(
            ReportService reportService,
            int page
    ) {
        this.reportService = reportService;
        this.page = page;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public int getPage() {
        return page;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
