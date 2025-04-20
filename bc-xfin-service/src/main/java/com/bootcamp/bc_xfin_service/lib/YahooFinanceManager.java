package com.bootcamp.bc_xfin_service.lib;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.bc_xfin_service.config.HttpHeadersConfig;
import com.bootcamp.bc_xfin_service.model.dto.QuoteDto;
import com.bootcamp.bc_xfin_service.model.dto.YahooFinanceChartDto;

@Service
public class YahooFinanceManager {
    private final CrumbManager crumbManager;
    private final CookieManager cookieManager;
    private final RestTemplate restTemplate;
    private final HttpHeadersConfig httpHeadersConfig;

    public YahooFinanceManager(CrumbManager crumbManager, CookieManager cookieManager, RestTemplate restTemplate, HttpHeadersConfig httpHeadersConfig) {
        this.crumbManager = crumbManager;
        this.cookieManager = cookieManager;
        this.restTemplate = restTemplate;
        this.httpHeadersConfig = httpHeadersConfig;
    }

    public QuoteDto quote(String symbol) {
        String crumb = crumbManager.getCrumb();
        String cookie = cookieManager.getCookie();

        if (crumb == null || crumb.isEmpty()) {
            crumbManager.refreshCrumb();
            crumb = crumbManager.getCrumb();
        }

        String url = String.format("https://query1.finance.yahoo.com/v7/finance/quote?symbols=%s&crumb=%s", symbol, crumb);
        
        HttpHeaders headers = new HttpHeaders(httpHeadersConfig.getHeaders());
        headers.set("Cookie", cookie);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<QuoteDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, QuoteDto.class);
        
        return response.getBody();
    }

    public YahooFinanceChartDto getHistoricalData(String symbol, Long startingPoint, Long endpoint) {
        String crumb = crumbManager.getCrumb();
        String cookie = cookieManager.getCookie();

        if (crumb == null || crumb.isEmpty()) {
            crumbManager.refreshCrumb();
            crumb = crumbManager.getCrumb();
        }

        String url = String.format("https://query1.finance.yahoo.com/v8/finance/chart/%s?period1=%s&period2=%s&interval=1d&events=history&crumb=%s", symbol, startingPoint, endpoint, crumb);
        
        HttpHeaders headers = new HttpHeaders(httpHeadersConfig.getHeaders());
        headers.set("Cookie", cookie);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<YahooFinanceChartDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, YahooFinanceChartDto.class);
        
        return response.getBody();
    }
}


