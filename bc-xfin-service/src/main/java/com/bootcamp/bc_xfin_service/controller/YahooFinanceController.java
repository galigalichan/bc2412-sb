package com.bootcamp.bc_xfin_service.controller;

import org.springframework.web.bind.annotation.*;

import com.bootcamp.bc_xfin_service.lib.CrumbManager;
import com.bootcamp.bc_xfin_service.lib.YahooFinanceManager;
import com.bootcamp.bc_xfin_service.model.dto.QuoteDto;
import com.bootcamp.bc_xfin_service.model.dto.YahooFinanceChartDto;

@RestController
@RequestMapping("/api/yahoo")
public class YahooFinanceController {
    private final YahooFinanceManager yahooFinanceManager;
    private final CrumbManager crumbManager;

    public YahooFinanceController(YahooFinanceManager yahooFinanceManager, CrumbManager crumbManager) {
        this.yahooFinanceManager = yahooFinanceManager;
        this.crumbManager = crumbManager;
    }

    @GetMapping(value = "/crumb")
    public String getCrumb() {
        return crumbManager.getCrumb();
    }

    @GetMapping(value = "/quote")
    public QuoteDto getQuote(@RequestParam String symbol) {
        return yahooFinanceManager.quote(symbol);
    }

    @GetMapping(value = "/pastdata")
    public YahooFinanceChartDto getHistoricalData(@RequestParam String symbol, @RequestParam Long start, @RequestParam Long end) {
        return yahooFinanceManager.getHistoricalData(symbol, start, end);
    }
}