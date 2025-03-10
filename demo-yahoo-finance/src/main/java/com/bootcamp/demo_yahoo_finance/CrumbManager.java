package com.bootcamp.demo_yahoo_finance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CrumbManager {
    private final CookieManager cookieManager;
    private final String userAgent;
    private String crumb;

    public CrumbManager(CookieManager cookieManager, String userAgent) {
        this.cookieManager = cookieManager;
        this.userAgent = userAgent;
        refreshCrumb();
    }

    public void refreshCrumb() {
        try {
            String cookie = cookieManager.getCookie();
            URL url = new URL("https://query1.finance.yahoo.com/v1/test/getcrumb");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", userAgent);
            connection.setRequestProperty("Cookie", cookie);

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed to fetch crumb: " + connection.getResponseMessage());
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                crumb = reader.readLine();
                System.out.println("New crumb: " + crumb);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch crumb: " + e.getMessage(), e);
        }
    }

    public String getCrumb() {
        return crumb;
    }
}


