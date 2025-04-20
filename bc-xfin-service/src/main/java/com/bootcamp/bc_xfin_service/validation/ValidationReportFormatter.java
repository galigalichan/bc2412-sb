package com.bootcamp.bc_xfin_service.validation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bootcamp.bc_xfin_service.model.dto.OhlcvRecordDTO;

@Service
public class ValidationReportFormatter {
    private static final Logger log = LoggerFactory.getLogger(ValidationReportFormatter.class);
    private static final DateTimeFormatter tsFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter fileTsFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(ZoneId.systemDefault());

    public void printAndSaveReport(
        Map<String, Set<Long>> missingTimestampsMap,
        Map<String, Set<Long>> extraTimestampsMap,
        Map<String, List<OhlcvRecordDTO>> corruptedRecordsMap,
        Map<String, List<TimestampConflict>> timestampConflictsMap,
        Map<String, List<OhlcvPatchSuggestion>> appliedFixesMap,
        Map<String, List<OhlcvPatchSuggestion>> failedFixesMap
    ) {
        StringBuilder report = new StringBuilder();
        report.append("=== Data Validation Report ===\nGenerated at: ").append(tsFormatter.format(Instant.now())).append("\n\n");

        // Missing
        report.append("--- Missing Timestamps ---\n");
        if (missingTimestampsMap.isEmpty()) {
            report.append("None\n");
        } else {
            missingTimestampsMap.forEach((symbol, timestamps) -> {
                report.append(String.format("Symbol: %s → %d missing:\n", symbol, timestamps.size()));
                timestamps.stream()
                        .sorted()
                        .forEach(ts -> report.append("  - ").append(format(ts)).append(" (").append(ts).append(")\n"));
            });
        }
        report.append("\n");

        // Extra
        report.append("--- Extra Timestamps ---\n");
        if (extraTimestampsMap.isEmpty()) {
            report.append("None\n");
        } else {
            extraTimestampsMap.forEach((symbol, timestamps) -> {
                report.append(String.format("Symbol: %s → %d extra:\n", symbol, timestamps.size()));
                timestamps.stream()
                        .sorted()
                        .forEach(ts -> report.append("  - ").append(format(ts)).append(" (").append(ts).append(")\n"));
            });
        }
        report.append("\n");

        // Corrupted
        report.append("--- Corrupted Records ---\n");
        if (corruptedRecordsMap.isEmpty()) {
            report.append("None\n");
        } else {
            corruptedRecordsMap.forEach((symbol, records) -> {
                report.append(String.format("Symbol: %s → %d corrupted:\n", symbol, records.size()));
                records.forEach(r -> report.append(String.format("  - %s (%d) → O: %s, H: %s, L: %s, C: %s, V: %s\n",
                        format(r.getTimestamp()), r.getTimestamp(),
                        r.getOpen(), r.getHigh(), r.getLow(), r.getClose(), r.getVolume())));
            });
        }
        report.append("\n");

        // Timestamp Conflicts
        report.append("--- Timestamp Conflicts ---\n");
        if (timestampConflictsMap.isEmpty()) {
            report.append("None\n");
        } else {
            timestampConflictsMap.forEach((symbol, conflictList) -> {
                for (TimestampConflict conflict : conflictList) {
                    report.append(String.format("Symbol: %s → Mismatch on %s\n", symbol, conflict.date()));
                    report.append("  DB timestamps:\n");
                    conflict.dbTimestamps().stream().sorted()
                            .forEach(ts -> report.append("    - ").append(format(ts)).append(" (").append(ts).append(")\n"));
                    report.append("  Yahoo timestamps:\n");
                    conflict.yahooTimestamps().stream().sorted()
                            .forEach(ts -> report.append("    - ").append(format(ts)).append(" (").append(ts).append(")\n"));
                }
            });
        }
        report.append("\n");

        // Auto-Fixes
        report.append("\n--- Applied Auto-Fixes ---\n");
        if (appliedFixesMap == null || appliedFixesMap.isEmpty()) {
            report.append("None\n");
        } else {
            appliedFixesMap.forEach((symbol, fixes) -> {
                report.append(String.format("Symbol: %s → %d fixed:\n", symbol, fixes.size()));
                for (OhlcvPatchSuggestion suggestion : fixes) {
                    report.append(String.format(
                        "  - ID: %d → %s → %s\n",
                        suggestion.id(),
                        format(suggestion.oldTimestamp()),
                        format(suggestion.newTimestamp())
                    ));
                }
            });
        }
        report.append("\n");

        // Failed Auto-Fixes
        report.append("\n--- Failed Auto-Fixes ---\n");
        if (failedFixesMap == null || failedFixesMap.isEmpty()) {
            report.append("None\n");
        } else {
            failedFixesMap.forEach((symbol, failures) -> {
                report.append(String.format("Symbol: %s → %d failed:\n", symbol, failures.size()));
                for (OhlcvPatchSuggestion fail : failures) {
                    report.append(String.format(
                        "  - ID: %d → %s → %s\n",
                        fail.id(),
                        format(fail.oldTimestamp()),
                        format(fail.newTimestamp())
                    ));
                }
            });
        }

        int totalApplied = appliedFixesMap != null
            ? appliedFixesMap.values().stream().mapToInt(List::size).sum()
            : 0;
        int totalFailed = failedFixesMap != null
            ? failedFixesMap.values().stream().mapToInt(List::size).sum()
            : 0;
        report.append(String.format("\n=== Auto-Fix Summary: %d applied, %d failed ===\n", totalApplied, totalFailed));

        // Final output
        String finalReport = report.toString();
        log.warn("\n{}", finalReport);
        saveReportToFile(finalReport);
    }

    private String format(Long ts) {
        return tsFormatter.format(Instant.ofEpochSecond(ts));
    }

    private void saveReportToFile(String report) {
        try {
            Path dir = Path.of("validation-reports");
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            String fileName = "validation-report-" + fileTsFormatter.format(Instant.now()) + ".txt";
            Path filePath = dir.resolve(fileName);

            Files.writeString(filePath, report);
            log.info("Validation report saved to: {}", filePath.toAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to save validation report: {}", e.getMessage(), e);
        }
    }
}