package com.bootcamp.sb.final_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.final_project.dto.QuoteDto;
import com.bootcamp.sb.final_project.service.YahooFinanceManager;

@RestController
public class YahooFinanceController {
    @Autowired
    private YahooFinanceManager yahooFinanceManager;

    @GetMapping(value = "/quote")
    public QuoteDto quote(@RequestParam String symbol) {
        return this.yahooFinanceManager.quote(symbol);
    }
    
}
