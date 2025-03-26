package com.bootcamp.bc_xfin_service.lib;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.bootcamp.bc_xfin_service.config.AppConfig;

public class CookieManagerTest {
    public static void main(String[] args) {
        // Load the Spring context
        @SuppressWarnings("resource")
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the CookieManager bean
        CookieManager cookieManager = context.getBean(CookieManager.class);

        // Call the refreshCookie method to test it
        try {
            cookieManager.refreshCookie();
            System.out.println("Cookie fetched successfully: " + cookieManager.getCookie());
        } catch (Exception e) {
            System.err.println("Error fetching cookie: " + e.getMessage());
        }
    }
}
