package com.bootcamp.sb.demo_sb_customer.config;
import java.math.BigDecimal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.sb.demo_sb_customer.codewave.RedisManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration // bean
public class AppConfig {
    // ! More than one method for creating beans
    @Bean
    BigDecimal BigDecimal() {
        return BigDecimal.valueOf(10);
    }

    @Bean
    String tutor() {
        return "Vincent";
    }

    // @Bean
    // String tutor2() {
    //     return "Lucas";
    // }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    // key, value <String, String>
    // Spring find the parameter dependency automatically
    // @Bean
    // RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
    //     RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    //     redisTemplate.setConnectionFactory(factory);
    //     redisTemplate.setKeySerializer(RedisSerializer.string());
    //     redisTemplate.setValueSerializer(RedisSerializer.json());
    //     redisTemplate.afterPropertiesSet();
    //     return redisTemplate; 
    // }

    @Bean
    RedisManager redisManager(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        return new RedisManager(factory, objectMapper);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
