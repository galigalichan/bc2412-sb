package com.bootcamp.bc_xfin_service.config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bootcamp.bc_xfin_service.entity.OHLCVEntity;
import com.bootcamp.bc_xfin_service.lib.YahooFinanceManager;
import com.bootcamp.bc_xfin_service.model.dto.OhlcvRecordDTO;
import com.bootcamp.bc_xfin_service.model.dto.YahooFinanceChartDto;
import com.bootcamp.bc_xfin_service.service.impl.OHLCVServiceImpl;
import com.bootcamp.bc_xfin_service.validation.AutoFixResult;
import com.bootcamp.bc_xfin_service.validation.OhlcvPatchSuggestion;
import com.bootcamp.bc_xfin_service.validation.TimestampConflict;
import com.bootcamp.bc_xfin_service.validation.ValidationReportFormatter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataValidatorRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataValidatorRunner.class);

    private final OHLCVServiceImpl ohlcvService;
    private final YahooFinanceManager yahooFinanceManager;
    private final ValidationReportFormatter reportFormatter;
    private final StocksProperties stocksProperties;
    @Value("${validation.auto-fix-enabled:false}")
    private boolean autoFixEnabled;
    
    @Override
    public void run(String... args) {
        runValidation(); // still runs at startup
    }

    public void runValidation() {
        List<String> stocks = stocksProperties.getSymbols();
        if (stocks == null || stocks.isEmpty()) {
            throw new IllegalStateException("Stock symbol list is empty or not configured.");
        }
    
        LocalDate start = LocalDate.of(2015, 1, 1);
        LocalDate end = LocalDate.now();
    
        Map<String, Set<Long>> missingTimestampsMap = new HashMap<>();
        Map<String, Set<Long>> extraTimestampsMap = new HashMap<>();
        Map<String, List<OhlcvRecordDTO>> corruptedRecordsMap = new HashMap<>();
        Map<String, List<TimestampConflict>> timestampConflictsMap = new HashMap<>();
        Map<String, List<OhlcvPatchSuggestion>> appliedFixesMap = new LinkedHashMap<>();
        Map<String, List<OhlcvPatchSuggestion>> failedFixesMap = new LinkedHashMap<>();
    
        for (String symbol : stocks) {
            long startEpoch = start.atStartOfDay(ZoneId.of("Asia/Hong_Kong")).toEpochSecond();
            long endEpoch = end.atStartOfDay(ZoneId.of("Asia/Hong_Kong")).toEpochSecond();
    
            YahooFinanceChartDto apiData = yahooFinanceManager.getHistoricalData(symbol, startEpoch, endEpoch);
            List<OhlcvRecordDTO> externalRecords = ohlcvService.mapToOhlcvRecords(apiData);
            List<OhlcvRecordDTO> dbRecords = ohlcvService.getOhlcvRecordsFromDb(symbol, startEpoch, endEpoch);
    
            System.out.printf("Validating %s: %d records in DB%n", symbol, dbRecords.size());
    
            List<TimestampConflict> conflicts = detectAndFilterTimestampConflict(symbol, externalRecords, dbRecords);
            if (!conflicts.isEmpty()) {
                timestampConflictsMap.put(symbol, conflicts);

                List<OhlcvPatchSuggestion> suggestions = new ArrayList<>();

                for (TimestampConflict conflict : conflicts) {
                    LocalDate conflictDate = conflict.date();
    
                    // Extract by date
                    long dateStart = conflictDate.atStartOfDay(ZoneId.of("Asia/Hong_Kong")).toEpochSecond();
                    long dateEnd = conflictDate.plusDays(1).atStartOfDay(ZoneId.of("Asia/Hong_Kong")).toEpochSecond() - 1;
    
                    List<OHLCVEntity> dbEntities = ohlcvService.getEntitiesBySymbolAndTimestamps(symbol, new ArrayList<>(conflict.dbTimestamps()))
                            .stream()
                            .filter(e -> e.getTimestamp() >= dateStart && e.getTimestamp() <= dateEnd)
                            .sorted(Comparator.comparing(OHLCVEntity::getTimestamp)) // ensure order
                            .toList();
    
                    List<Long> yahooTimestamps = conflict.yahooTimestamps().stream()
                            .filter(ts -> ts >= dateStart && ts <= dateEnd)
                            .sorted()
                            .toList();
    
                    int size = Math.min(dbEntities.size(), yahooTimestamps.size());
                    for (int i = 0; i < size; i++) {
                        OHLCVEntity entity = dbEntities.get(i);
                        suggestions.add(new OhlcvPatchSuggestion(entity.getId(), entity.getTimestamp(), yahooTimestamps.get(i)));
                    }
                }
                
                if (autoFixEnabled) {
                    AutoFixResult fixResult = ohlcvService.applyAutoFix(suggestions);
                    appliedFixesMap.put(symbol, fixResult.applied());
                    failedFixesMap.put(symbol, fixResult.failed());

                    log.warn("🎉 Auto-fix complete for '{}': {} applied, {} failed",
                            symbol, fixResult.applied().size(), fixResult.failed().size());
                }
            }

            Set<Long> apiTimestamps = externalRecords.stream().map(OhlcvRecordDTO::getTimestamp).collect(Collectors.toSet());
            Set<Long> dbTimestamps = dbRecords.stream().map(OhlcvRecordDTO::getTimestamp).collect(Collectors.toSet());
    
            Set<Long> missingTimestamps = new HashSet<>(apiTimestamps);
            missingTimestamps.removeAll(dbTimestamps);
    
            Set<Long> extraTimestamps = new HashSet<>(dbTimestamps);
            extraTimestamps.removeAll(apiTimestamps);
    
            List<OhlcvRecordDTO> corrupted = dbRecords.stream()
                .filter(r -> r.getOpen() == null || r.getClose() == null || r.getHigh() == null || r.getLow() == null || r.getVolume() == null)
                .collect(Collectors.toList());
    
            if (!missingTimestamps.isEmpty()) missingTimestampsMap.put(symbol, missingTimestamps);
            if (!extraTimestamps.isEmpty()) extraTimestampsMap.put(symbol, extraTimestamps);
            if (!corrupted.isEmpty()) corruptedRecordsMap.put(symbol, corrupted);
        }
    
        boolean hasCriticalIssues = !missingTimestampsMap.isEmpty() || !extraTimestampsMap.isEmpty() || !corruptedRecordsMap.isEmpty();
        boolean hasTimestampConflicts = !timestampConflictsMap.isEmpty();
    
        if (hasCriticalIssues || hasTimestampConflicts) {
            reportFormatter.printAndSaveReport(
                missingTimestampsMap,
                extraTimestampsMap,
                corruptedRecordsMap,
                timestampConflictsMap,
                appliedFixesMap,
                failedFixesMap
            );
    
            if (hasCriticalIssues) {
                    System.out.println("⚠️ Critical validation issues found. Starting in recovery mode.");
            } else {
                System.out.println("⚠️ Timestamp conflicts detected — not blocking startup.");
            }
        } else {
            System.out.println("✅ All symbols validated successfully.");
        }        
    }
    
    private List<TimestampConflict> detectAndFilterTimestampConflict(
        String symbol,
        List<OhlcvRecordDTO> externalRecords,
        List<OhlcvRecordDTO> dbRecords
    ) {
        Map<LocalDate, Set<Long>> yahooGrouped = groupByDate(externalRecords);
        Map<LocalDate, Set<Long>> dbGrouped = groupByDate(dbRecords);
    
        List<TimestampConflict> conflicts = new ArrayList<>();
    
        Set<LocalDate> allDates = new HashSet<>();
        allDates.addAll(yahooGrouped.keySet());
        allDates.addAll(dbGrouped.keySet());
    
        for (LocalDate date : allDates) {
            Set<Long> yahooTimestamps = yahooGrouped.getOrDefault(date, Set.of());
            Set<Long> dbTimestamps = dbGrouped.getOrDefault(date, Set.of());
    
            // Only consider it a conflict if both Yahoo and DB have data for this date
            if (!yahooTimestamps.isEmpty() && !dbTimestamps.isEmpty() && !yahooTimestamps.equals(dbTimestamps)) {
                conflicts.add(new TimestampConflict(date, dbTimestamps, yahooTimestamps));
    
                // Remove from both lists so they don’t show up in missing/extra check
                long startEpoch = date.atStartOfDay(ZoneId.of("Asia/Hong_Kong")).toEpochSecond();
                long endEpoch = date.plusDays(1).atStartOfDay(ZoneId.of("Asia/Hong_Kong")).toEpochSecond() - 1;
    
                externalRecords.removeIf(r -> r.getTimestamp() >= startEpoch && r.getTimestamp() <= endEpoch);
                dbRecords.removeIf(r -> r.getTimestamp() >= startEpoch && r.getTimestamp() <= endEpoch);
            }
        }
    
        return conflicts;
    }
        
    private Map<LocalDate, Set<Long>> groupByDate(List<OhlcvRecordDTO> records) {
        return records.stream()
            .collect(Collectors.groupingBy(
                r -> Instant.ofEpochSecond(r.getTimestamp())
                            .atZone(ZoneId.of("Asia/Hong_Kong"))
                            .toLocalDate(),
                Collectors.mapping(OhlcvRecordDTO::getTimestamp, Collectors.toSet())
            ));
    }

}
