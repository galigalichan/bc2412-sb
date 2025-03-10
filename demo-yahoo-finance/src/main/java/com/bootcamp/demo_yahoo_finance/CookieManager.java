package com.bootcamp.demo_yahoo_finance;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.List;
import java.util.Map;

public class CookieManager {
    private String cookie;
    private final String userAgent;

    public CookieManager(String userAgent) {
        this.userAgent = userAgent;
        refreshCookie();
    }

    public void refreshCookie() {
        try {
            URL url = new URL("https://fc.yahoo.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", userAgent);

            Map<String, List<String>> headers = connection.getHeaderFields();
            List<String> cookies = headers.get("Set-Cookie");

            if (cookies != null) {
                // Join all cookies into a single string
                cookie = String.join("; ", cookies);
                System.out.println("New cookie: " + cookie);
            } else {
                throw new RuntimeException("Failed to get cookies");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error fetching Yahoo Finance cookie: " + e.getMessage(), e);
        }
    }

    public String getCookie() {
        return cookie;
    }
}


