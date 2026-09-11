package de.lifemytouch.ansi.report;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report createReport(Player reporter, Player target, ReportCategory reportCategory, String reason) {
        if(reporter.getUniqueId().equals(target.getUniqueId()))
            throw new IllegalArgumentException(
                "Du kannst dich nicht selbst melden!"
            );

        if(reportCategory == null)
            throw new IllegalArgumentException(
                "Keine Report-Kategorie angegeben"
            );

        if(reason == null || reason.isBlank())
            throw new IllegalArgumentException(
                    "Kein Report-Grund angegeben!"
            );

        Report report = new Report(
                reportRepository.nextId(),
                target.getUniqueId(),
                reporter.getUniqueId(),
                reportCategory,
                reason,
                System.currentTimeMillis(),
                ReportStatus.PENDING,
                null
        );

        reportRepository.save(report);

        return report;
    }

    public Report getReport(long id) {
        return reportRepository.findById(id);
    }

    public List<Report> getOpenReports() {
        return reportRepository.findByStatus(ReportStatus.PENDING);
    }

    public List<Report> getReportsInReview() {
        return reportRepository.findByStatus(ReportStatus.IN_REVIEW);
    }

    public void takeReport(long id, UUID moderator) {
        Report report = requireReport(id);

        report.setStatus(ReportStatus.IN_REVIEW);
        report.setModerator(moderator);

        reportRepository.save(report);
    }

    public void resolveReport(long id, UUID moderator) {
        Report report = requireReport(id);

        report.setStatus(ReportStatus.RESOLVED);
        report.setModerator(moderator);

        reportRepository.save(report);
    }

    public void dismissReport(long id, UUID moderator) {
        Report report = requireReport(id);

        report.setStatus(ReportStatus.DISMISSED);
        report.setModerator(moderator);

        reportRepository.save(report);
    }

    private Report requireReport(long id) {
        Report report = reportRepository.findById(id);

        if(report == null)
            throw new IllegalArgumentException(
                    "Report #" + id + " wurde nicht gefunden!"
            );

        return report;
    }

}
