package com.bootcamp.bc_xfin_web.service;

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

import com.bootcamp.bc_xfin_web.model.CandleStick;

@Service
public class OhlcvDataService {
    private static final Logger logger = LoggerFactory.getLogger(OhlcvDataService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bc.xfin.service.url}") // Load from application.yml
    private String backendUrl;

    public OhlcvDataService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Map<String, List<CandleStick>> getCandlesByDay(String symbol) {
        // Fetch from backend
        String interval = "daily";
        Map<String, Object> response = fetchFromBackend(symbol, interval);
        logger.info("Fetched Backend Response: {}", response);

        // Extract and parse candle stick data
        Object candleSticksRaw = response.get("candleSticks");
        List<CandleStick> candleSticks = parseResponseToCandleSticks(candleSticksRaw);
        
        logger.info("Parsed daily candle sticks: {}", candleSticks);
    
        // Return the processed data
        return Map.of("candleSticks", candleSticks);
    }

    public Map<String, List<CandleStick>> getCandlesByWeek(String symbol) {
        // Fetch from backend
        String interval = "weekly";
        Map<String, Object> response = fetchFromBackend(symbol, interval);
        logger.info("Fetched Backend Response: {}", response);

        // Extract and parse candle stick data
        Object candleSticksRaw = response.get("candleSticks");
        List<CandleStick> candleSticks = parseResponseToCandleSticks(candleSticksRaw);
        
        logger.info("Parsed weekly candle sticks: {}", candleSticks);
    
        // Return the processed data
        return Map.of("candleSticks", candleSticks);
    }

    public Map<String, List<CandleStick>> getCandlesByMonth(String symbol) {
        // Fetch from backend
        String interval = "monthly";
        Map<String, Object> response = fetchFromBackend(symbol, interval);
        logger.info("Fetched Backend Response: {}", response);

        // Extract and parse candle stick data
        Object candleSticksRaw = response.get("candleSticks");
        List<CandleStick> candleSticks = parseResponseToCandleSticks(candleSticksRaw);
        
        logger.info("Parsed monthly candle sticks: {}", candleSticks);
    
        // Return the processed data
        return Map.of("candleSticks", candleSticks);
    }
    
    public Map<String, Object> fetchFromBackend(String symbol, String interval) {
        String url = backendUrl + "/api/ohlcv/" + interval + "/" + symbol;
        logger.info("Fetching data from backend: {}", url);

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
    }

    private List<CandleStick> parseResponseToCandleSticks(Object rawData) {
        if (!(rawData instanceof List<?> rawList)) {
            logger.warn("Data list is empty in parseResponseToCandleSticks!");
            return Collections.emptyList();
        }

        return rawList.stream()
            .map(item -> {
                if (item instanceof Map<?, ?> dataMap) {
                    Object timestampRaw = dataMap.get("timestamp");
                    Object openRaw = dataMap.get("open");
                    Object highRaw = dataMap.get("high");
                    Object lowRaw = dataMap.get("low");
                    Object closeRaw = dataMap.get("close");
                    Object volumeRaw = dataMap.get("volume");
                    Object sma10Raw = dataMap.get("sma10");
                    Object sma20Raw = dataMap.get("sma20");
                    Object sma30Raw = dataMap.get("sma30");
                    Object sma50Raw = dataMap.get("sma50");
                    Object sma100Raw = dataMap.get("sma100");
                    Object sma150Raw = dataMap.get("sma150");

                    Long timestamp = timestampRaw instanceof Number num ? num.longValue() : null;
                    Double open = openRaw instanceof Number num ? num.doubleValue() : null;
                    Double high = highRaw instanceof Number num ? num.doubleValue() : null;
                    Double low = lowRaw instanceof Number num ? num.doubleValue() : null;
                    Double close = closeRaw instanceof Number num ? num.doubleValue() : null;
                    Long volume = volumeRaw instanceof Number num ? num.longValue() : null;
                    Double sma10 = sma10Raw instanceof Number num ? num.doubleValue() : null;
                    Double sma20 = sma20Raw instanceof Number num ? num.doubleValue() : null;
                    Double sma30 = sma30Raw instanceof Number num ? num.doubleValue() : null;
                    Double sma50 = sma50Raw instanceof Number num ? num.doubleValue() : null;
                    Double sma100 = sma100Raw instanceof Number num ? num.doubleValue() : null;
                    Double sma150 = sma150Raw instanceof Number num ? num.doubleValue() : null;

                    if (timestamp != null && open != null && high != null && low != null && close != null && volume != null) {
                        return new CandleStick(timestamp, open , high, low, close, volume, sma10, sma20, sma30, sma50, sma100, sma150);
                    }
                }
                logger.warn("Invalid data entry in candlestick list: {}", item);
                return new CandleStick(); // Empty object if conversion fails
            })
            .collect(Collectors.toList());
    }    
}
