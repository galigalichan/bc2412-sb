package com.bootcamp.bc_xfin_service.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.bc_xfin_service.lib.YahooFinanceManager;
import com.bootcamp.bc_xfin_service.model.dto.BatchRecoveryRequest;
import com.bootcamp.bc_xfin_service.model.dto.RecoveryRequest;
import com.bootcamp.bc_xfin_service.service.impl.OHLCVServiceImpl;
import com.bootcamp.bc_xfin_service.validation.OhlcvPatchSuggestion;

@RestController
@RequestMapping("/api/recover")
public class OhlcvRecoveryController {

    private final OHLCVServiceImpl ohlcvService;
    private final YahooFinanceManager yahooFinanceManager;

    @Value("${recovery.secret-token}")
    private String secretToken;

    public OhlcvRecoveryController(OHLCVServiceImpl ohlcvService, YahooFinanceManager yahooFinanceManager) {
        this.ohlcvService = ohlcvService;
        this.yahooFinanceManager = yahooFinanceManager;
    }

    /**
     * ✅ Endpoint: Batch recovery from Yahoo Finance
     */
    @PostMapping("/batch")
    public ResponseEntity<?> recoverBatch(@RequestBody BatchRecoveryRequest request) {
        List<String> results = new ArrayList<>();

        for (RecoveryRequest recovery : request.getRecoveries()) {
            String symbol = recovery.getSymbol();
            long start = recovery.getStartEpoch();
            long end = recovery.getEndEpoch();

            try {
                var chartDto = yahooFinanceManager.getHistoricalData(symbol, start, end);
                var records = ohlcvService.mapToOhlcvRecords(chartDto);
                ohlcvService.saveOhlcvRecords(records);
                results.add("✅ Recovered: " + symbol);
            } catch (Exception e) {
                results.add("❌ Failed: " + symbol + " - " + e.getMessage());
            }
        }

        return ResponseEntity.ok(results);
    }

    /**
     * ✅ Endpoint: PATCH a single timestamp
     * Example: PATCH /api/recover/timestamp?id=123&newTimestamp=1681234567
     */
    @PatchMapping("/timestamp")
    public ResponseEntity<String> patchTimestamp(
        @RequestHeader("X-SECRET-TOKEN") String secret,
        @RequestParam Long id,
        @RequestParam Long newTimestamp
    ) {
        if (!secretToken.equals(secret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
        }

        boolean updated = ohlcvService.updateTimestampById(id, newTimestamp);
        if (updated) {
            return ResponseEntity.ok("✅ Timestamp updated.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Record not found.");
        }
    }

    /**
     * ✅ Endpoint: Bulk PATCH timestamps by ID
     * Body: List of OhlcvPatchSuggestion(id, newTimestamp)
     */
    @PatchMapping("/update-timestamps")
    public ResponseEntity<?> updateTimestamps(
        @RequestHeader("X-SECRET-TOKEN") String secret,
        @RequestBody List<OhlcvPatchSuggestion> suggestions
    ) {
        if (!secretToken.equals(secret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
        }

        int success = 0;
        List<Long> failedIds = new ArrayList<>();

        for (OhlcvPatchSuggestion suggestion : suggestions) {
            try {
                boolean updated = ohlcvService.updateTimestampById(suggestion.id(), suggestion.newTimestamp());
                if (updated) {
                    success++;
                } else {
                    failedIds.add(suggestion.id());
                }
            } catch (Exception e) {
                failedIds.add(suggestion.id());
            }
        }

        return ResponseEntity.ok(Map.of(
            "updated", success,
            "failed", failedIds
        ));
    }
}