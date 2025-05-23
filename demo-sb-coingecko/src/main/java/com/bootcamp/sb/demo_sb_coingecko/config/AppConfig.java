package com.bootcamp.sb.demo_sb_coingecko.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.sb.demo_sb_coingecko.lib.RedisManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

@Configuration
public class AppConfig {
    @Bean(name = "vincent")
    RestTemplate restTemplate2() {
        // Connection Pool: in-built queuing / time-out algorithm 
        return new RestTemplate();
    }

    @Bean(name = "lucas")
    RestTemplate restTemplate1() {
        // Connection Pool: in-built queuing / time-out algorithm 
        return new RestTemplate();
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    // ! for Spring boot use (RestTemplate, Controller serialization)
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper() //
                .registerModule(new ParameterNamesModule()) //
                .registerModule(new Jdk8Module()) //
                .registerModule(new JavaTimeModule());

    }

    @Bean
    RedisManager redisManager(RedisConnectionFactory factory, ObjectMapper objectMapper) {
    // ObjectMapper objectMapper = new ObjectMapper() //
    //             .registerModule(new ParameterNamesModule()) //
    //             .registerModule(new Jdk8Module()) //
    //             .registerModule(new JavaTimeModule());
        return new RedisManager(factory, objectMapper);
    }

}
