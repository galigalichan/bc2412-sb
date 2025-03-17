package com.bootcamp.bc_xfin_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching // Enables caching support in Spring Boot
@EnableScheduling // Enable scheduling in the application
public class BcXfinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BcXfinServiceApplication.class, args);
	}

}
