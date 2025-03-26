package com.bootcamp.bc_xfin_web.service;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.bc_xfin_web.model.LinePoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class StockDataService {
    private static final Logger logger = LoggerFactory.getLogger(StockDataService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${bc.xfin.service.url}") // Load from application.yml
    private String backendUrl;

    public StockDataService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Map<String, List<LinePoint>> getPricePointByFiveMinute(String symbol) {
        String redisKey = "5MIN-" + symbol;

        // Retrieve from Redis
        String cachedJson = (String) redisTemplate.opsForValue().get(redisKey);
        if (cachedJson != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule()); // Handle date/time
                logger.info("Retrieved from Redis: {}", cachedJson);
                return objectMapper.readValue(cachedJson, new TypeReference<Map<String, List<LinePoint>>>() {});
            } catch (JsonProcessingException e) {
                logger.error("Error parsing Redis JSON", e);
            }
        }

        // Fetch from backend
        Map<String, Object> response = fetchFromBackend(symbol);
        logger.info("Fetched Backend Response: {}", response);

        // Extract stock price data
        Object stockDataRaw = response.get("5MIN-" + symbol);
        if (stockDataRaw == null || !(stockDataRaw instanceof Map)) {
            logger.warn("No valid stock data found for key: 5MIN-{}", symbol);
            return Collections.emptyMap();
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> stockDataMap = (Map<String, Object>) stockDataRaw;
        Object dataRaw = stockDataMap.get("data");
        Object movingAvgRaw = stockDataMap.get("movingAverages");

        List<LinePoint> pricePoints = parseResponseToLinePoints(dataRaw);
        List<LinePoint> movingAverages = parseResponseToLinePoints(movingAvgRaw);

        logger.info("Parsed price points: {}", pricePoints);
        logger.info("Parsed moving averages: {}", movingAverages);

        // Store in Redis
        Map<String, List<LinePoint>> result = new HashMap<>();
        result.put("priceData", pricePoints);
        result.put("movingAverage", movingAverages);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String jsonData = objectMapper.writeValueAsString(result);
            redisTemplate.opsForValue().set(redisKey, jsonData, Duration.ofMinutes(5));
        } catch (JsonProcessingException e) {
            logger.error("Error serializing data to JSON", e);
        }

        return result;
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
                    Object timestampRaw = dataMap.get("regularMarketTime");
                    Object closeRaw = dataMap.get("regularMarketPrice");

                    Long timestamp = timestampRaw instanceof Number num ? num.longValue() : null;
                    Double close = closeRaw instanceof Number num ? num.doubleValue() : null;

                    if (timestamp != null && close != null) {
                        return new LinePoint(timestamp, close);
                    }
                }
                logger.warn("Invalid data entry in stock price list: {}", item);
                return new LinePoint(); // Empty object if conversion fails
            })
            .collect(Collectors.toList());
    }
}