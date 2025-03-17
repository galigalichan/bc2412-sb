package com.bootcamp.bc_xfin_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.bc_xfin_service.config.HttpHeadersConfig;

@Service
public class CrumbManager {
    private static final Logger logger = LoggerFactory.getLogger(CrumbManager.class);
    private static final String CRUMB_URL = "https://query1.finance.yahoo.com/v1/test/getcrumb";
    
    private String crumb;
    private final RestTemplate restTemplate;
    private final CookieManager cookieManager;
    private final HttpHeadersConfig httpHeadersConfig;

    public CrumbManager(RestTemplate restTemplate, CookieManager cookieManager, HttpHeadersConfig httpHeadersConfig) {
        this.restTemplate = restTemplate;
        this.cookieManager = cookieManager;
        this.httpHeadersConfig = httpHeadersConfig;
    }

    public void refreshCrumb() {
        try {
            HttpHeaders headers = new HttpHeaders(httpHeadersConfig.getHeaders());
            headers.set("Cookie", cookieManager.getCookie());

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(CRUMB_URL, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                crumb = response.getBody();
                logger.info("Successfully retrieved crumb.");
            } else {
                throw new RuntimeException("Failed to fetch crumb, received status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error fetching Yahoo Finance crumb: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching Yahoo Finance crumb: " + e.getMessage(), e);
        }
    }

    public String getCrumb() {
        if (crumb == null) {
            refreshCrumb();
        }
        return crumb;
    }
}
