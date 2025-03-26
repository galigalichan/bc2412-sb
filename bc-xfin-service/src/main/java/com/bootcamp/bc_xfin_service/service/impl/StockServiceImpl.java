package com.bootcamp.bc_xfin_service.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.bc_xfin_service.entity.TStockEntity;
import com.bootcamp.bc_xfin_service.entity.TStocksPriceEntity;
import com.bootcamp.bc_xfin_service.lib.RedisManager;
import com.bootcamp.bc_xfin_service.model.dto.FiveMinData;
import com.bootcamp.bc_xfin_service.repository.TStockRepository;
import com.bootcamp.bc_xfin_service.repository.TStocksPriceRepository;
import com.bootcamp.bc_xfin_service.service.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StockServiceImpl implements StockService {
    private static final Logger logger = LoggerFactory.getLogger(StockPriceServiceImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private TStockRepository tStockRepository;

    @Autowired
    private TStocksPriceRepository tStocksPriceRepository;

    private List<String> getStockListFromRedis(String key) {
        try {
            String json = redisManager.get(key, String.class); // Get the raw JSON string
            if (json == null) {
                return null;
            }
            return objectMapper.readValue(json, new TypeReference<List<String>>() {}); // Deserialize into List<String>
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, List<String>> getStockList() {
        String redisKey = "STOCK-LIST";
        List<String> stockListFromRedis = this.getStockListFromRedis(redisKey);

        if (stockListFromRedis != null) {
            return Collections.singletonMap("STOCK-LIST", stockListFromRedis);
        }

        // Fetch from DB if not found in Redis
        List<TStockEntity> tStockFromDB = tStockRepository.findAll();
        List<String> stockListFromDB = tStockFromDB.stream()
                .map(TStockEntity::getSymbol)
                .collect(Collectors.toList());

        try {
            // Store in Redis with 24-hour expiry
            redisManager.set(redisKey, stockListFromDB, Duration.ofHours(24));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Collections.singletonMap("STOCK-LIST", stockListFromDB);
    }

    
    private LocalDate getSystemDateFromRedis(String key) {
        try {
            String json = redisManager.get(key, String.class);
            if (json == null) {
                return null;
            }
            return objectMapper.readValue(json, LocalDate.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, String> getSystemDate(String symbol) {
        String redisKey = "SYSDATE-".concat(symbol);

        // Check Redis first
        LocalDate systemDateFromRedis = getSystemDateFromRedis(redisKey);
        if (systemDateFromRedis != null) {
            logger.info("System date for {} retrieved from Redis: {}", symbol, systemDateFromRedis);
            return Collections.singletonMap(redisKey, systemDateFromRedis.toString()); // Convert to String
        }

        // Fetch max regularMarketTime from DB
        Optional<TStocksPriceEntity> latestStock = tStocksPriceRepository.findTopBySymbolOrderByRegularMarketTimeDesc(symbol);

        if (latestStock.isPresent()) {
            LocalDate systemDateFromDB = Instant.ofEpochSecond(latestStock.get().getRegularMarketTime())
                                                .atZone(ZoneId.of("Asia/Hong_Kong"))
                                                .toLocalDate();
            logger.info("System date for {} retrieved from DB: {}", symbol, systemDateFromDB);

            try {
                redisManager.set(redisKey, systemDateFromDB, Duration.ofHours(8));
                logger.info("System date for {} cached in Redis: {}", symbol, systemDateFromDB);
            } catch (JsonProcessingException e) {
                logger.error("Failed to cache system date in Redis for {}: {}", symbol, e.getMessage());
            }

            return Collections.singletonMap(redisKey, systemDateFromDB.toString()); // Convert to String
        }

        logger.warn("No system date found for symbol: {}", symbol);
        return Collections.singletonMap(redisKey, null);
    }

    public Map<String, Object> get5MinData(String symbol) {
        String redisKey = "5MIN-".concat(symbol);
    
        try {
            // Retrieve cached data from Redis
            FiveMinData cachedData = redisManager.get(redisKey, FiveMinData.class);
            Set<Long> cachedTimestamps = new HashSet<>();
    
            if (cachedData != null) {
                cachedTimestamps = cachedData.getData().stream()
                    .map(TStocksPriceEntity::getRegularMarketTime)
                    .collect(Collectors.toSet());
            }
    
            // Get system date
            Map<String, String> sysDateMap = getSystemDate(symbol);
            String systemDateString = sysDateMap.get("SYSDATE-" + symbol);
            if (systemDateString == null) {
                logger.warn("No system date found for symbol: {}", symbol);
                return Collections.singletonMap(redisKey, Collections.emptyList());
            }
    
            LocalDate systemDate = LocalDate.parse(systemDateString);
            logger.info("System date for {}: {}", symbol, systemDate);
    
            // Fetch all available 5-min stock data from the database
            List<TStocksPriceEntity> dbData = tStocksPriceRepository.findBySymbolAndMarketDateDesc(symbol, systemDate);
            if (dbData.isEmpty()) {
                logger.warn("No 5-min stock data found for {} on {}", symbol, systemDate);
                return Collections.singletonMap(redisKey, cachedData != null ? cachedData : Collections.emptyList());
            }
    
            final Set<Long> finalCachedTimestamps = cachedTimestamps; // Create a final reference

            // Identify missing timestamps
            List<TStocksPriceEntity> missingData = dbData.stream() // Use final reference
                .filter(entry -> !finalCachedTimestamps.contains(entry.getRegularMarketTime()))
                .collect(Collectors.toList());
    
            // If there are missing data points, update Redis
            if (!missingData.isEmpty()) {
                List<TStocksPriceEntity> updatedData = new ArrayList<>();
                updatedData.addAll(missingData);
    
                // Sort in descending order by market time
                updatedData.sort(Comparator.comparing(TStocksPriceEntity::getRegularMarketTime).reversed());

                // Store updated data in Redis for 12 hours
                FiveMinData newData = new FiveMinData(updatedData.get(0).getRegularMarketTime(), updatedData);
                redisManager.set(redisKey, newData, Duration.ofHours(12));

                return Collections.singletonMap(redisKey, newData);
            }

            return Collections.singletonMap(redisKey, cachedData != null ? cachedData : dbData);

        } catch (JsonProcessingException e) {
            logger.error("Failed to deserialize Redis data for {}: {}", symbol, e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error retrieving 5-min data for {}: {}", symbol, e.getMessage());
        }

        return Collections.singletonMap(redisKey, Collections.emptyList());
    }

    public List<TStocksPriceEntity> calculate3DayMovingAverage(String symbol) {
        // ✅ Fetch latest 300 records in DESCENDING order (most recent first)
        List<TStocksPriceEntity> recentPrices = 
            tStocksPriceRepository.findTop300BySymbolOrderByRegularMarketTimeDesc(symbol);
    
        if (recentPrices.size() < 198) {
            logger.warn("Not enough data points for symbol {} to calculate 3-day moving average", symbol);
            return Collections.emptyList();
        }
    
        List<TStocksPriceEntity> movingAverages = new ArrayList<>();
        double sum = 0;
    
        // Compute the initial sum for the first 198 most recent points
        for (int i = 0; i < 198; i++) {
            sum += recentPrices.get(i).getRegularMarketPrice();
        }
    
        // ✅ Compute moving averages starting from the most recent data
        for (int i = 0; i <= (recentPrices.size() - 198); i++) { 
            double avg = sum / 198;

            TStocksPriceEntity maEntry = new TStocksPriceEntity();
            maEntry.setRegularMarketTime(recentPrices.get(i).getRegularMarketTime()); // ✅ Align with 5-min data
            maEntry.setRegularMarketPrice(avg);
            movingAverages.add(maEntry);

            // ✅ Slide the window to keep processing the latest data
            if (i + 198 < recentPrices.size()) {
                sum -= recentPrices.get(i).getRegularMarketPrice();  // Remove the oldest value
                sum += recentPrices.get(i + 198).getRegularMarketPrice();  // Add the next new value
            }
        }
    
        logger.info("Calculated {} moving average points for symbol {}", movingAverages.size(), symbol);
        return movingAverages;
    }
    
    public Map<String, Object> get5MinDataWithMA(String symbol) {
        Map<String, Object> response = new HashMap<>();
    
        // Fetch 5-min stock data
        Map<String, Object> stockData = get5MinData(symbol);
        response.put("stockData", stockData.values().iterator().next());
    
        // Get 3-day moving average time series
        List<TStocksPriceEntity> movingAverageList = calculate3DayMovingAverage(symbol);
        response.put("movingAverages", movingAverageList);
    
        return response;
    }
    
}

    

