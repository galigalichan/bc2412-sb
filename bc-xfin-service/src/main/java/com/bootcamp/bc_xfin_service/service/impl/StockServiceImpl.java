package com.bootcamp.bc_xfin_service.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.bootcamp.bc_xfin_service.model.FiveMinData;
import com.bootcamp.bc_xfin_service.model.LinePoint;
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
            String dateStr = redisManager.get(key, String.class);
            if (dateStr == null) {
                return null;
            }
            return LocalDate.parse(dateStr); // Directly parse instead of using ObjectMapper
        } catch (Exception e) {
            logger.error("Failed to retrieve system date from Redis for {}: {}", key, e.getMessage());
            return null;
        }
    }

    @Override
    public Map<String, String> getSystemDate(String symbol) {
        String redisKey = "SYSDATE-".concat(symbol);
    
        // Try fetching from Redis
        LocalDate systemDate = getSystemDateFromRedis(redisKey);
        if (systemDate != null) {
            logger.info("System date for {} retrieved from Redis: {}", symbol, systemDate);
            return Collections.singletonMap(redisKey, systemDate.toString());
        }
    
        // Try fetching from the database
        Optional<TStocksPriceEntity> latestStock = tStocksPriceRepository.findTopBySymbolOrderByRegularMarketTimeDesc(symbol);
        if (latestStock.isPresent()) {
            systemDate = Instant.ofEpochSecond(latestStock.get().getRegularMarketTime())
                                .atZone(ZoneId.of("Asia/Hong_Kong"))
                                .toLocalDate();
            logger.info("System date for {} retrieved from DB: {}", symbol, systemDate);
        } else {
            // If no data in DB, use today's date
            systemDate = LocalDate.now(ZoneId.of("Asia/Hong_Kong"));
            logger.warn("No system date found for symbol: {}. Using today's date: {}", symbol, systemDate);
        }
    
        // Cache the result in Redis for 8 hours
        try {
            redisManager.set(redisKey, systemDate.toString(), Duration.ofHours(8));
            logger.info("System date for {} cached in Redis: {}", symbol, systemDate);
        } catch (JsonProcessingException e) {
            logger.error("Failed to cache system date in Redis for {}: {}", symbol, e.getMessage());
        }
    
        return Collections.singletonMap(redisKey, systemDate.toString());
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
    
                // Sort in ascending order by market time
                updatedData.sort(Comparator.comparing(TStocksPriceEntity::getRegularMarketTime));

                // Store updated data in Redis for 12 hours
                FiveMinData newData = new FiveMinData(updatedData.get(updatedData.size() - 1).getRegularMarketTime(), updatedData);
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

    public Map<String, Object> get5MinDataWithMA(String symbol) throws JsonProcessingException {
        // Fetch today's 5-min data
        Map<String, Object> fiveMinDataResponse = get5MinData(symbol);
        if (!fiveMinDataResponse.containsKey("5MIN-" + symbol)) {
            return Collections.emptyMap();
        }
        
        Object rawData = fiveMinDataResponse.get("5MIN-" + symbol);
        FiveMinData fiveMinData;
        
        if (rawData instanceof List<?>) {
            // If it's a list, set it as the data field
            fiveMinData = new FiveMinData();
            fiveMinData.setData(objectMapper.convertValue(rawData, new TypeReference<List<TStocksPriceEntity>>() {}));
        } else {
            fiveMinData = objectMapper.convertValue(rawData, FiveMinData.class);
        }
        
        List<TStocksPriceEntity> stockDataList = fiveMinData.getData();
        if (stockDataList == null || stockDataList.isEmpty()) {
            return Collections.emptyMap();
        }

        // Find the earliest timestamp from today's data
        long earliestTimestamp = stockDataList.get(0).getRegularMarketTime();
        logger.info("Earliest timestamp for {}: {}", symbol, earliestTimestamp);
        
        // Retrieve the 329 preceding entries from the database
        List<TStocksPriceEntity> historicalData = tStocksPriceRepository.findPreviousEntries(symbol, earliestTimestamp, 329);
        
        // Reverse to ensure chronological order (oldest first)
        Collections.reverse(historicalData);
        logger.info("Historical data count for {}: {}", symbol, historicalData.size());

        // Combine historical and today's data
        List<TStocksPriceEntity> fullData = new ArrayList<>(historicalData);
        fullData.addAll(stockDataList);
        logger.info("Full data count (historical + today's) for {}: {}", symbol, fullData.size());

        // Prepare list of line points for response
        List<LinePoint> linePoints = new ArrayList<>();
    
        // If not enough historical data, return today's prices with null MA
        if (fullData.size() < 330) {
            logger.warn("Not enough data to calculate MA for {}. Data count: {}", symbol, fullData.size());
            for (TStocksPriceEntity entry : stockDataList) {
                linePoints.add(new LinePoint(entry.getRegularMarketTime(), entry.getRegularMarketPrice(), null));
            }
            return Map.of("linePoints", linePoints);
        }
        
        // Calculate moving averages using a sliding window
        double sum = fullData.subList(0,329).stream().mapToDouble(TStocksPriceEntity::getRegularMarketPrice).sum();
        for (int i = 329; i < fullData.size(); i++) {
            TStocksPriceEntity currentEntry = fullData.get(i);
            sum += currentEntry.getRegularMarketPrice();
            double movingAverage = sum / 330;

            linePoints.add(new LinePoint(currentEntry.getRegularMarketTime(), currentEntry.getRegularMarketPrice(), movingAverage));
            
            // Subtract the earliest value in the window to maintain the rolling sum
            sum -= fullData.get(i - 329).getRegularMarketPrice();
        }
        
        logger.info("Generated {} line points for {}", linePoints.size(), symbol);
        
        // Cache result in Redis for 5 minutes
        redisManager.set("LINEPT-" + symbol, linePoints, Duration.ofMinutes(5));
        
        return Map.of("linePoints", linePoints);
    }
    
}

    

