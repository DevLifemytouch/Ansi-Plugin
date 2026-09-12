package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.punish.PunishmentCategory;
import de.lifemytouch.ansi.punish.PunishmentService;
import de.lifemytouch.ansi.report.ReportCategory;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ReportPunishmentHolder implements InventoryHolder {

    private final long reportId;
    private final ReportService reportService;
    private final PunishmentCategory reportCategory;

    public ReportPunishmentHolder(long reportId, ReportService reportService, PunishmentCategory reportCategory) {
        this.reportId = reportId;
        this.reportService = reportService;
        this.reportCategory = reportCategory;
    }

    public long getReportId() {
        return reportId;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public PunishmentCategory getCategory() {
        return reportCategory;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
