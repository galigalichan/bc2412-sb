package com.bootcamp.bc_xfin_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bootcamp.bc_xfin_service.service.impl.StockServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/yahoo")
public class StockController {
    @Autowired
    private StockServiceImpl stockServiceImpl;
    
    @GetMapping(value = "/stocklist")
    public Map<String, List<String>> getStockList() {
        return this.stockServiceImpl.getStockList();
    }

    @GetMapping(value = "/sysdate")
    public Map<String, String> getSystemDate(@RequestParam String symbol) {
        return this.stockServiceImpl.getSystemDate(symbol);
    }

    @GetMapping(value = "/5mindata")
    public Map<String, Object> get5MinData(@RequestParam String symbol) {
        return this.stockServiceImpl.get5MinData(symbol);
    }

}

