package com.bootcamp.bc_xfin_service.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.bc_xfin_service.service.impl.OHLCVServiceImpl;

@RestController
@RequestMapping("/api/ohlcv")
public class OHLCVController {
    private final OHLCVServiceImpl ohlcvServiceImpl;

    public OHLCVController(OHLCVServiceImpl ohlcvServiceImpl) {
        this.ohlcvServiceImpl = ohlcvServiceImpl;
    }

    @PostMapping("/fetch-and-save")
    public ResponseEntity<String> fetchAndSaveHistorical() {
        File lockFile = new File("fetch-lock.txt");

        if (lockFile.exists()) {
            return ResponseEntity.status(403).body("Historical fetch has already been executed.");
        }

        // Run the historical fetch logic
        ohlcvServiceImpl.fetchAndSaveHistorical();

        // Create lock file and write timestamp
        try (FileWriter writer = new FileWriter(lockFile)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("Historical fetch was triggered on: " + timestamp + System.lineSeparator());
            return ResponseEntity.ok("Fetched and saved historical OHLCV data.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred during locking.");
        }
    }
}