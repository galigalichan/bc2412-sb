package com.bootcamp.bc_xfin_service.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.bc_xfin_service.entity.TStockEntity;
import com.bootcamp.bc_xfin_service.entity.TStocksPriceEntity;
import com.bootcamp.bc_xfin_service.infra.RedisManager;
import com.bootcamp.bc_xfin_service.model.dto.FiveMinData;
import com.bootcamp.bc_xfin_service.repository.TStockRepository;
import com.bootcamp.bc_xfin_service.repository.TStocksPriceRepository;
import com.bootcamp.bc_xfin_service.service.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StockServiceImpl implements StockService {
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
        LocalDate systemDateFromRedis = getSystemDateFromRedis(redisKey);

        if (systemDateFromRedis != null) {
            return Collections.singletonMap(redisKey, systemDateFromRedis.toString()); // Convert to String
        }

        // Fetch max regularMarketTime from DB
        Optional<TStocksPriceEntity> latestStock = tStocksPriceRepository.findTopBySymbolOrderByRegularMarketTimeDesc(symbol);

        if (latestStock.isPresent()) {
            LocalDate systemDateFromDB = Instant.ofEpochSecond(latestStock.get().getRegularMarketTime())
                                                .atZone(ZoneId.of("Asia/Hong_Kong"))
                                                .toLocalDate();

            try {
                redisManager.set(redisKey, systemDateFromDB, Duration.ofHours(8));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return Collections.singletonMap(redisKey, systemDateFromDB.toString()); // Convert to String
        }

        return Collections.singletonMap(redisKey, null);
    }

    @Override
    public Map<String, Object> get5MinData(String symbol) {
        String redisKey = "5MIN-".concat(symbol);

        try {
            String cachedJson = redisManager.get(redisKey, String.class);
            FiveMinData cachedData = (cachedJson != null) ? objectMapper.readValue(cachedJson, FiveMinData.class) : null;

            // Get system date as String and convert it back to LocalDate
            String systemDateString = getSystemDate(symbol).get("SYSDATE-".concat(symbol));
            if (systemDateString == null) {
                return Collections.singletonMap(redisKey, Collections.emptyList());
            }

            LocalDate systemDate = LocalDate.parse(systemDateString); // Convert String back to LocalDate

            // Fetch all 5-min data on the same system date
            List<TStocksPriceEntity> fiveMinData = tStocksPriceRepository.findBySymbolAndMarketDateDesc(symbol, systemDate);

            if (!fiveMinData.isEmpty()) {
                long latestMarketTime = fiveMinData.get(0).getRegularMarketTime(); // Get the most recent entry

                if (cachedData != null && cachedData.getRegularMarketTime() >= latestMarketTime) {
                    return Collections.singletonMap(redisKey, cachedData);
                }

                FiveMinData newData = new FiveMinData(latestMarketTime, fiveMinData);
                redisManager.set(redisKey, newData, Duration.ofHours(12));

                return Collections.singletonMap(redisKey, newData);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Collections.singletonMap(redisKey, Collections.emptyList());
    }

}

    

