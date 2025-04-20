package com.bootcamp.bc_xfin_web;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class BcXfinWebApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Hong_Kong")); // Force app to use Hong Kong time
		SpringApplication.run(BcXfinWebApplication.class, args);
	}

}
