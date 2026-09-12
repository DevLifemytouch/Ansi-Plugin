package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.punish.PunishmentCategory;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ReportPunishmentTypeHolder implements InventoryHolder {

    private final long reportId;
    private final ReportService reportService;
    private final PunishmentCategory punishmentCategory;

    public ReportPunishmentTypeHolder(
            long reportId,
            ReportService reportService,
            PunishmentCategory punishmentCategory
    ) {
        this.reportId = reportId;
        this.reportService = reportService;
        this.punishmentCategory = punishmentCategory;
    }

    public long getReportId() {
        return reportId;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public PunishmentCategory getPunishmentCategory() {
        return punishmentCategory;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}