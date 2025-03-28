package com.bootcamp.bc_xfin_web.service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.bc_xfin_web.lib.RedisManager;
import com.bootcamp.bc_xfin_web.model.LinePoint;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class StockDataService {
    private static final Logger logger = LoggerFactory.getLogger(StockDataService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisManager redisManager;

    @Value("${bc.xfin.service.url}") // Load from application.yml
    private String backendUrl;

    public StockDataService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Map<String, List<LinePoint>> getPricePointByFiveMinute(String symbol) {
        String redisKey = "LINEPT-" + symbol;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Handle date/time properly
    
        // 1️⃣ Retrieve from Redis with error handling
        Map<String, List<LinePoint>> cachedData = null;
        try {
            cachedData = redisManager.get(redisKey, new TypeReference<>() {});
            if (cachedData != null && !cachedData.isEmpty()) {
                logger.info("Returning cached data for symbol: {}", symbol);
                return cachedData;
            }
        } catch (Exception e) {
            logger.error("Error retrieving or parsing data from Redis for key {}: {}", redisKey, e.getMessage());
        }
    
        // 2️⃣ Fetch from backend if no valid cached data
        Map<String, Object> response = fetchFromBackend(symbol);
        logger.info("Fetched Backend Response: {}", response);

        // 3️⃣ Extract and parse stock price data
        Object linePointsRaw = response.get("linePoints");
        List<LinePoint> linePoints = parseResponseToLinePoints(linePointsRaw);
        
        logger.info("Parsed line points: {}", linePoints);
    
        // 4️⃣ Store processed data in Redis
        try {
            redisManager.set(redisKey, Map.of("linePoints", linePoints), Duration.ofMinutes(5));
        } catch (Exception e) {
            logger.error("Error storing data in Redis", e);
        }
    
        // 5️⃣ Return the processed data
        return Map.of("linePoints", linePoints);
    }
    
    public Map<String, Object> fetchFromBackend(String symbol) {
        String url = backendUrl + "/api/yahoo/5min/" + symbol;
        logger.info("Fetching data from backend: {}", url);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                logger.info("Successfully fetched data for symbol: {}", symbol);
                return response.getBody();
            } else {
                logger.error("Backend request failed: {}", response.getStatusCode());
                throw new RuntimeException("Backend request failed: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error fetching 5-min stock data from backend: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching 5-min stock data from backend: " + e.getMessage(), e);
        }
    }

    private List<LinePoint> parseResponseToLinePoints(Object rawData) {
        if (!(rawData instanceof List<?> rawList)) {
            logger.warn("Data list is empty in parseResponseToLinePoints!");
            return Collections.emptyList();
        }

        return rawList.stream()
            .map(item -> {
                if (item instanceof Map<?, ?> dataMap) {
                    Object timestampRaw = dataMap.get("timestamp");
                    Object closeRaw = dataMap.get("close");
                    Object movingAvgRaw = dataMap.get("movingAverage");

                    Long timestamp = timestampRaw instanceof Number num ? num.longValue() : null;
                    Double close = closeRaw instanceof Number num ? num.doubleValue() : null;
                    Double movingAverage = movingAvgRaw instanceof Number num ? num.doubleValue() : null;

                    if (timestamp != null && close != null) {
                        return new LinePoint(timestamp, close, movingAverage);
                    }
                }
                logger.warn("Invalid data entry in stock price list: {}", item);
                return new LinePoint(); // Empty object if conversion fails
            })
            .collect(Collectors.toList());
    }
}