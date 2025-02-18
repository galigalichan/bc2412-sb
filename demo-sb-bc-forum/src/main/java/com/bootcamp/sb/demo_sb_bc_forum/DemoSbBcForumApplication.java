package com.bootcamp.sb.demo_sb_bc_forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // tell Spring Boot not to configure a datasource automatically
public class DemoSbBcForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSbBcForumApplication.class, args);
	}

}
