package com.bootcamp.bc_xfin_service.config;

import com.bootcamp.bc_xfin_service.lib.CookieInterceptor;
import com.bootcamp.bc_xfin_service.lib.CookieManager;
import com.bootcamp.bc_xfin_service.lib.HttpHeadersConfig;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory; // or JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

    @SuppressWarnings("removal")
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(RedisSerializer.string());
        
        // Explicitly set custom ObjectMapper for Redis serialization
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        serializer.setObjectMapper(objectMapper);

        template.setValueSerializer(serializer);
        return template;
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Handles Java 8 Date/Time types
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Keeps ISO-8601 format
        return objectMapper;
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
