package com.bootcamp.bc_xfin_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bootcamp.bc_xfin_service.service.impl.StockServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // Allow cross-origin requests (adjust if needed)
public class StockController {
    @Autowired
    private StockServiceImpl stockServiceImpl;
    
    @GetMapping(value = "/stocklist", produces = MediaType.APPLICATION_JSON_VALUE) // for explicit JSON response
    public ResponseEntity<Map<String, List<String>>> getStockList() {
        return ResponseEntity.ok(stockServiceImpl.getStockList());
    }

    @GetMapping(value = "/sysdate", produces = MediaType.APPLICATION_JSON_VALUE) // for explicit JSON response
    public ResponseEntity<Map<String, String>> getSystemDate(@RequestParam String symbol) {
        return ResponseEntity.ok(stockServiceImpl.getSystemDate(symbol));
    }

    @GetMapping(value = "/5mindata", produces = MediaType.APPLICATION_JSON_VALUE) // for explicit JSON response
    public ResponseEntity<Map<String, Object>> get5MinData(@RequestParam String symbol) {
        return ResponseEntity.ok(stockServiceImpl.get5MinData(symbol));
    }

    @GetMapping(value = "/5min/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE) // for explicit JSON response
    public ResponseEntity<Map<String, Object>> get5MinDataWithMA(@PathVariable String symbol) throws JsonProcessingException {
        return ResponseEntity.ok(stockServiceImpl.get5MinDataWithMA(symbol));
    }

}

