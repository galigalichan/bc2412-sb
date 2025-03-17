package com.bootcamp.bc_xfin_service.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.bc_xfin_service.config.HttpHeadersConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;

@Service
public class CookieManager {
    private static final Logger logger = LoggerFactory.getLogger(CookieManager.class);
    private String cookie;
    private final RestTemplate restTemplate;
    @SuppressWarnings("unused")
    private final ObjectMapper objectMapper;
    private final HttpHeadersConfig httpHeadersConfig;

    public CookieManager(RestTemplate restTemplate, ObjectMapper objectMapper, HttpHeadersConfig httpHeadersConfig) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.httpHeadersConfig = httpHeadersConfig;
    }

    @SuppressWarnings("deprecation")
    public void refreshCookie() {
        try {
            String url = "https://finance.yahoo.com"; // Ensure this is the correct URL
            HttpEntity<String> entity = new HttpEntity<>(httpHeadersConfig.getHeaders());
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            // Log response details
            logger.info("Response Code: {}", response.getStatusCodeValue());
            logger.info("Response Headers: {}", response.getHeaders());
            logger.info("Response Body: {}", response.getBody());
            
            if (response.getStatusCode() == HttpStatus.OK) {
                List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                if (cookies != null && !cookies.isEmpty()) {
                    cookie = String.join("; ", cookies);
                    logger.info("Cookies fetched: {}", cookie);
                } else {
                    throw new RuntimeException("Failed to get cookies");
                }
            } else {
                throw new RuntimeException("Failed to fetch cookie, received status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error fetching Yahoo Finance cookie: {}", e.getMessage());
            throw new RuntimeException("Error fetching Yahoo Finance cookie: " + e.getMessage(), e);
        }
    }

    public void refreshCookieWithSelenium() {
        WebDriver driver = new ChromeDriver(); // Make sure you have the appropriate WebDriver set up
        driver.get("https://finance.yahoo.com");
        
        // Wait for page to load and potentially set cookies
        // (Add explicit wait if necessary)
        
        Set<Cookie> cookies = driver.manage().getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
        }
        
        driver.quit();
    }

    public String getCookie() {
        if (cookie == null || cookie.isEmpty()) {
            refreshCookie();
        }
        return cookie;
    }
}
