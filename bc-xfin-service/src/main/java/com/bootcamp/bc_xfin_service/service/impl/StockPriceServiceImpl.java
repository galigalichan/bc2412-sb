package com.bootcamp.bc_xfin_service.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bootcamp.bc_xfin_service.codewave.Type;
import com.bootcamp.bc_xfin_service.config.RedisManager;
import com.bootcamp.bc_xfin_service.config.StocksProperties;
import com.bootcamp.bc_xfin_service.entity.TStocksPriceEntity;
import com.bootcamp.bc_xfin_service.lib.YahooFinanceManager;
import com.bootcamp.bc_xfin_service.repository.TStocksPriceRepository;
import com.bootcamp.bc_xfin_service.service.HolidayService;
import com.bootcamp.bc_xfin_service.service.StockPriceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.bootcamp.bc_xfin_service.model.FiveMinData;
import com.bootcamp.bc_xfin_service.model.dto.QuoteDto;
import com.bootcamp.bc_xfin_service.model.dto.QuoteDto.QuoteResponse.Result;
import com.bootcamp.bc_xfin_service.model.dto.StocksPriceDTO;

@Service
public class StockPriceServiceImpl implements StockPriceService {
    private static final Logger logger = LoggerFactory.getLogger(StockPriceServiceImpl.class);
    private final StocksProperties stocksProperties;

    @Autowired
    private TStocksPriceRepository stocksPriceRepository;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private YahooFinanceManager yahooFinanceManager;

    @Autowired
    private HolidayService holidayService;

    public StockPriceServiceImpl(StocksProperties stocksProperties) {
        this.stocksProperties = stocksProperties;
    }

    @Scheduled(cron = "0 50 9  * * MON-FRI")
    @Scheduled(cron = "0 55 9  * * MON-FRI")
    @Scheduled(cron = "0 0/5 10-11 * * MON-FRI")
    @Scheduled(cron = "0 0 12  * * MON-FRI")
    @Scheduled(cron = "0 5 12  * * MON-FRI")
    @Scheduled(cron = "0 10 12  * * MON-FRI")
    @Scheduled(cron = "0 15 12  * * MON-FRI")
    @Scheduled(cron = "0 20 13  * * MON-FRI")
    @Scheduled(cron = "0 25 13  * * MON-FRI")
    @Scheduled(cron = "0 30 13  * * MON-FRI")
    @Scheduled(cron = "0 35 13  * * MON-FRI")
    @Scheduled(cron = "0 40 13  * * MON-FRI")
    @Scheduled(cron = "0 45 13  * * MON-FRI")
    @Scheduled(cron = "0 50 13  * * MON-FRI")
    @Scheduled(cron = "0 55 13  * * MON-FRI")
    @Scheduled(cron = "0 0/5 14-15 * * MON-FRI")
    @Scheduled(cron = "0 0 16  * * MON-FRI")
    @Scheduled(cron = "0 5 16  * * MON-FRI")
    @Scheduled(cron = "0 10 16  * * MON-FRI")
    @Scheduled(cron = "0 15 16  * * MON-FRI")
    public void recordStockPrices() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Hong_Kong"));

        // Check if today is a public holiday
        if (holidayService.isHoliday(today)) {
            logger.info("Stock market is closed today (public holiday). Skipping data retrieval.");
            return; // Skip this execution
        }

        List<String> stocks = stocksProperties.getSymbols();

        for (String symbol : stocks) {
            // Fetch the stock price data
            StocksPriceDTO stockData = fetchStockData(symbol);
            if (stockData == null) {
                logger.warn("Skipping symbol {} as no stock data was retrieved.", symbol);
                continue;
            }
            
            // Set currTime to the API call time
            LocalDateTime currTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"));
            
            // Convert regularMarketTime to LocalDateTime and set marketUnixTime
            LocalDateTime marketUnixTime = convertToLocalDateTime(stockData.getRegularMarketTime());
    
            TStocksPriceEntity entity = new TStocksPriceEntity();
            // Set the entity properties from DTO
            entity.setSymbol(stockData.getSymbol());
            entity.setRegularMarketTime(stockData.getRegularMarketTime());
            entity.setRegularMarketPrice(stockData.getRegularMarketPrice());
            entity.setRegularMarketChangePercent(stockData.getRegularMarketChangePercent());
            entity.setBid(stockData.getBid());
            entity.setAsk(stockData.getAsk());
            entity.setType(Type.FIVEM); // Set type here
            entity.setCurrTime(currTime); // API call time
            entity.setMarketUnixTime(marketUnixTime); // Converted market time
    
            // Save to the database
            stocksPriceRepository.save(entity);
    
            // Save to Redis with exception handling
            try {
                String redisKey = "5MIN-" + symbol;
                FiveMinData fiveMinData = redisManager.get(redisKey, FiveMinData.class);
            
                List<TStocksPriceEntity> updatedData = (fiveMinData != null) ? new ArrayList<>(fiveMinData.getData()) : new ArrayList<>();
                updatedData.add(entity);
            
                FiveMinData newFiveMinData = new FiveMinData(stockData.getRegularMarketTime(), updatedData);
                
                // No need for manual serialization
                redisManager.set(redisKey, newFiveMinData, Duration.ofHours(12));
            } catch (JsonProcessingException e) {
                logger.error("Failed to save stock data to Redis for symbol {}: {}", symbol, e.getMessage());
            }
        }
    }

    private LocalDateTime convertToLocalDateTime(Long unixTime) {
        if (unixTime == null) {
            return null;
        }
    // Convert Unix time to LocalDateTime in Hong Kong time zone
        return LocalDateTime.ofInstant(
            Instant.ofEpochSecond(unixTime),
            ZoneId.of("Asia/Hong_Kong")
        );
    }

    private StocksPriceDTO fetchStockData(String symbol) {
        // Fetch the stock data from Yahoo Finance
        QuoteDto fullData = yahooFinanceManager.quote(symbol);
        
        // Check if the result list is not empty
        if (fullData.getQuoteResponse().getResult() == null || fullData.getQuoteResponse().getResult().isEmpty()) {
            // Handle the case where there are no results
            logger.warn("No data found for symbol: {}", symbol);
            return null; // or throw an exception, or return an empty DTO
        }
    
        // Access the first result
        Result result = fullData.getQuoteResponse().getResult().get(0);
        
        // Map the data to StocksPriceDTO
        StocksPriceDTO selectedData = new StocksPriceDTO();
        selectedData.setSymbol(result.getSymbol());
        selectedData.setRegularMarketTime(result.getRegularMarketTime());
        selectedData.setRegularMarketPrice(result.getRegularMarketPrice());
        selectedData.setRegularMarketChangePercent(result.getRegularMarketChangePercent());
        selectedData.setBid(result.getBid());
        selectedData.setAsk(result.getAsk());
        selectedData.setType(null); // Set to null for now
        selectedData.setCurrTime(null); // Set this later when saving to DB
        selectedData.setMarketUnixTime(null); // Set this later when saving to DB
    
        return selectedData;
    }

}
