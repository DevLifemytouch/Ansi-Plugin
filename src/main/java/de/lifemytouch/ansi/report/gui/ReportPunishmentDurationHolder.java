package de.lifemytouch.ansi.report.gui;

import de.lifemytouch.ansi.punish.PunishmentCategory;
import de.lifemytouch.ansi.punish.PunishmentType;
import de.lifemytouch.ansi.report.ReportService;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ReportPunishmentDurationHolder implements InventoryHolder {

    private final long reportId;
    private final ReportService reportService;
    private final PunishmentCategory punishmentCategory;
    private final PunishmentType punishmentType;
    private final String duration;

    public ReportPunishmentDurationHolder(
            long reportId,
            ReportService reportService,
            PunishmentCategory punishmentCategory,
            PunishmentType punishmentType,
            String duration
    ) {
        this.reportId = reportId;
        this.reportService = reportService;
        this.punishmentCategory = punishmentCategory;
        this.punishmentType = punishmentType;
        this.duration = duration;
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

    public PunishmentType getPunishmentType() {
        return punishmentType;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}