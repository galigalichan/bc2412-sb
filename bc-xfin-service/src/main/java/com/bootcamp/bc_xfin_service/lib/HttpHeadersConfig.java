package com.bootcamp.bc_xfin_service.lib;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class HttpHeadersConfig {
    private final HttpHeaders headers;

    public HttpHeadersConfig() {
        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
    }

    @Bean
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        headers.set("Accept-Language", "en-US,en;q=0.9");
        headers.set("Referer", "https://finance.yahoo.com/");
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        return headers;
    }
}
