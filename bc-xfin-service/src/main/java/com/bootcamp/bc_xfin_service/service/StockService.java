package com.bootcamp.bc_xfin_service.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface StockService {
    public Map<String, List<String>> getStockList();

    public Map<String, String> getSystemDate(String symbol);

    public Map<String, Object> get5MinData(String symbol);

    public Map<String, Object> get5MinDataWithMA(String symbol) throws JsonProcessingException;
}
