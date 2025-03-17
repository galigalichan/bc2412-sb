package com.bootcamp.bc_xfin_service.service;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class CookieFetcher {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        
        try {
            driver.get("https://finance.yahoo.com");

            // Wait for the page to load and cookies to be set
            Thread.sleep(5000); // Replace with explicit waits for production code

            // Fetch cookies
            Set<Cookie> cookies = driver.manage().getCookies();
            for (Cookie cookie : cookies) {
                System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}