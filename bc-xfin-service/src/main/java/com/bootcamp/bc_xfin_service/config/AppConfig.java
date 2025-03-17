package com.bootcamp.bc_xfin_service.config;

import com.bootcamp.bc_xfin_service.service.CookieInterceptor;
import com.bootcamp.bc_xfin_service.service.CookieManager; // Import your CookieManager

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory; // or JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class AppConfig {
    
    @Bean
    RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(); // Configure with your Redis server details
    }

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // Add the CookieInterceptor with an empty cookie list initially
        restTemplate.getInterceptors().add(new CookieInterceptor(new ArrayList<>()));
        return restTemplate;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        return template;
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Register the Java Time module
        return mapper; // Define the ObjectMapper bean
    }

    @Bean
    public HttpHeadersConfig httpHeadersConfig() {
        return new HttpHeadersConfig(); // Provide HttpHeadersConfig bean
    }

    @Bean
    public CookieManager cookieManager(RestTemplate restTemplate, ObjectMapper objectMapper, HttpHeadersConfig httpHeadersConfig) {
        return new CookieManager(restTemplate, objectMapper, httpHeadersConfig); // Pass HttpHeadersConfig
    }
}
