package de.lifemytouch.ansi.report;

import org.bukkit.entity.Player;

import java.util.Collection;
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

    public ReportClaimResult takeReport(long id, UUID moderator) {
        Report report = reportRepository.findById(id);

        if (report == null) {
            return ReportClaimResult.NOT_FOUND;
        }

        if (report.getStatus() == ReportStatus.PENDING) {
            report.setStatus(ReportStatus.IN_REVIEW);
            report.setModerator(moderator);
            reportRepository.save(report);
            return ReportClaimResult.CLAIMED;
        }

        if (report.getStatus() == ReportStatus.IN_REVIEW) {
            return moderator.equals(report.getModerator())
                    ? ReportClaimResult.ALREADY_CLAIMED_BY_YOU
                    : ReportClaimResult.CLAIMED_BY_OTHER;
        }

        return ReportClaimResult.NOT_OPEN;
    }

    public boolean resolveReport(long id, UUID moderator) {
        Report report = requireReport(id);

        if (!isClaimedBy(report, moderator)) {
            return false;
        }

        report.setStatus(ReportStatus.RESOLVED);
        reportRepository.save(report);
        return true;
    }

    public boolean dismissReport(long id, UUID moderator) {
        Report report = requireReport(id);

        if (!isClaimedBy(report, moderator)) {
            return false;
        }

        report.setStatus(ReportStatus.DISMISSED);
        reportRepository.save(report);
        return true;
    }

    public boolean isClaimedBy(long id, UUID moderator) {
        Report report = reportRepository.findById(id);
        return report != null && isClaimedBy(report, moderator);
    }

    public void releaseReportsClaimedBy(UUID moderator) {
        for (Report report : reportRepository.findByStatus(ReportStatus.IN_REVIEW)) {
            if (!moderator.equals(report.getModerator())) {
                continue;
            }

            report.setStatus(ReportStatus.PENDING);
            report.setModerator(null);
            reportRepository.save(report);
        }
    }

    private boolean isClaimedBy(Report report, UUID moderator) {
        return report.getStatus() == ReportStatus.IN_REVIEW
                && moderator.equals(report.getModerator());
    }

    private Report requireReport(long id) {
        Report report = reportRepository.findById(id);

        if(report == null)
            throw new IllegalArgumentException(
                    "Report #" + id + " wurde nicht gefunden!"
            );

        return report;
    }

    public Collection<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public List<Report> getReportsByStatus(ReportStatus status) {
        return reportRepository.findByStatus(status);
    }

}
