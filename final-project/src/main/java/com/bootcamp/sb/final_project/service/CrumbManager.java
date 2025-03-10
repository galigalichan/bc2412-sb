package com.bootcamp.sb.final_project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class CrumbManager {
    private final RestTemplate restTemplate; // address
    private final String cookie;
    private final String userAgent;

    public CrumbManager(RestTemplate restTemplate, 
                    @Value("${yahoo.finance.cookie}") String cookie,
                    @Value("${yahoo.finance.user-agent}") String userAgent) {
        this.restTemplate = restTemplate;
        this.cookie = cookie;
        this.userAgent = userAgent;
    }

    public String getCrumb() {
        try {
            String url = "https://query1.finance.yahoo.com/v1/test/getcrumb";
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", userAgent);
            headers.set("Cookie", cookie);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch crumb: " + e.getMessage(), e);
        }
    }

    public String getCookie() {
        return cookie;
    }

    public String getUserAgent() {
        return userAgent;
    }

}