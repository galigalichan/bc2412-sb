package com.bootcamp.sb.final_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.sb.final_project.dto.QuoteDto;

@Service
public class YahooFinanceManager {
    private final RestTemplate restTemplate;
    private final CrumbManager crumbManager;
    private final String cookie;
    private final String userAgent;

    @Autowired // Injects dependencies via the constructor
    public YahooFinanceManager(RestTemplate restTemplate, CrumbManager crumbManager) {
        this.restTemplate = restTemplate;
        this.crumbManager = crumbManager;
        this.cookie = crumbManager.getCookie(); // Get cookie from CrumbManager
        this.userAgent = crumbManager.getUserAgent(); // Get User-Agent from CrumbManager
    }

    public QuoteDto quote(String symbol) {
        // Retrieve crumb key dynamically
        String crumb = crumbManager.getCrumb();
        if (crumb == null || crumb.isEmpty()) {
            throw new RuntimeException("Failed to retrieve crumb key.");
        }

        // Construct the API URL dynamically
        String url = String.format("https://query1.finance.yahoo.com/v7/finance/quote?symbols=%s&crumb=%s", symbol, crumb);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", userAgent);
        headers.set("Cookie", cookie);

        // Create request entity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make API request
        ResponseEntity<QuoteDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, QuoteDto.class);

        return response.getBody();
    }
}