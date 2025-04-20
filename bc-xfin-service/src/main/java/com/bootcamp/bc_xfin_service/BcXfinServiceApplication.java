package com.bootcamp.bc_xfin_service;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching // Enables caching support in Spring Boot
@EnableScheduling // Enable scheduling in the application
@EnableConfigurationProperties
public class BcXfinServiceApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Hong_Kong")); // Force app to use Hong Kong time
		SpringApplication.run(BcXfinServiceApplication.class, args);
	}

}
