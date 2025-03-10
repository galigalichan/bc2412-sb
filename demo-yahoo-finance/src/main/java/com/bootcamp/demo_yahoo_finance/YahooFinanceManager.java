package com.bootcamp.demo_yahoo_finance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YahooFinanceManager {
    private final CrumbManager crumbManager;
    private final CookieManager cookieManager;
    private final String userAgent;

    public YahooFinanceManager(CrumbManager crumbManager, CookieManager cookieManager, String userAgent) {
        this.crumbManager = crumbManager;
        this.cookieManager = cookieManager;
        this.userAgent = userAgent;
    }

    public String quote(String symbol) {
        try {
            String crumb = crumbManager.getCrumb();
            String cookie = cookieManager.getCookie();

            if (crumb == null || crumb.isEmpty()) {
                crumbManager.refreshCrumb();
                crumb = crumbManager.getCrumb();
            }

            String url = String.format("https://query1.finance.yahoo.com/v7/finance/quote?symbols=%s&crumb=%s", symbol, crumb);
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", userAgent);
            connection.setRequestProperty("Cookie", cookie);

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed to fetch quote: " + connection.getResponseMessage());
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error fetching quote: " + e.getMessage(), e);
        }
    }
}

