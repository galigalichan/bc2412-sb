package com.bootcamp.sb.demo_sb_customer.config;
import java.math.BigDecimal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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
    
}
