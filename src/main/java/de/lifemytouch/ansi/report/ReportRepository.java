package de.lifemytouch.ansi.report;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ReportRepository {

    private final JavaPlugin javaPlugin;
    private final File dataFile;

    private final List<Report> reports = new ArrayList<>();

    private long nextId = 1;

    public ReportRepository(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        this.dataFile = new File(javaPlugin.getDataFolder(), "reports.yml");
    }

    public void save(Report report) {
        reports.removeIf(existing -> existing.getId() == report.getId());
        reports.add(report);

        saveFile();
    }

    public void delete(long id) {
        reports.removeIf(report -> report.getId() == id);
        saveFile();
    }

    public Report findById(long id) {
        return reports.stream()
                .filter(report -> report.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Collection<Report> findAll() {
        return List.copyOf(reports);
    }


    public List<Report> findByStatus(ReportStatus status) {
        return reports.stream()
                .filter(report -> report.getStatus() == status)
                .sorted(Comparator.comparingLong(Report::getId))
                .toList();
    }

    public long nextId() {
        return nextId++;
    }

    private void load() {
        reports.clear();

        if(!dataFile.exists()) return;

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(dataFile);

        nextId = fileConfiguration.getLong("nextId", 1);

        ConfigurationSection section = fileConfiguration.getConfigurationSection("reports");

        if(section == null) return;

        for(String key : section.getKeys(false)) {
            try {
                long id = Long.parseLong(key);

                String targetString = section.getString(key + ".target");
                String reporterString = section.getString(key + ".reporter");
                String categoryString = section.getString(key + ".category");
                String reason = section.getString(key + ".reason", "Unknown");
                long createdAt = section.getLong(key + ".createdAt");
                String statusString = section.getString(key + ".status", ReportStatus.PENDING.name());
                String moderatorString = section.getString(key + ".moderator");

                ReportCategory reportCategory;

                try {
                    reportCategory = ReportCategory.valueOf(categoryString);
                } catch (Exception exception) {
                    reportCategory = ReportCategory.OTHER;
                }

                ReportStatus reportStatus;

                try {
                    reportStatus = ReportStatus.valueOf(statusString);
                } catch (Exception exception) {
                    reportStatus = ReportStatus.PENDING;
                }

                Report report = new Report(
                        id,
                        UUID.fromString(targetString),
                        UUID.fromString(reporterString),
                        reportCategory,
                        reason,
                        createdAt,
                        reportStatus,
                        moderatorString != null
                                ? UUID.fromString(moderatorString)
                                : null
                );

                reports.add(report);

                if(id >= nextId) {
                    nextId = id + 1;
                }


            } catch (Exception exception) {
                javaPlugin.getLogger().warning("Konnte Report " + key + " nicht laden: " + exception.getMessage());
            }

        }
    }

    private void saveFile() {
        FileConfiguration fileConfiguration = new YamlConfiguration();

        fileConfiguration.set("nextId", nextId);

        for(Report report : reports) {
            String path = "reports." + report.getId();

            fileConfiguration.set(path + ".target", report.getTarget().toString());
            fileConfiguration.set(path + ".reporter", report.getReporter().toString());
            fileConfiguration.set(path + ".category", report.getReason());
            fileConfiguration.set(path + ".reason", report.getReason());
            fileConfiguration.set(path + ".createdAt", report.getCreatedAt());
            fileConfiguration.set(path + ".status", report.getStatus().name());
            fileConfiguration.set(path + ".moderator", report.getModerator() != null
                    ? report.getModerator().toString()
                    : null);

        }

        try {
            fileConfiguration.save(dataFile);
        } catch (IOException exception) {
            javaPlugin.getLogger().severe("Konnte reports.yml nicht speichern: " +
                    exception.getMessage());
        }

    }

}

