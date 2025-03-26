package com.bootcamp.bc_xfin_service.lib;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class CookieInterceptor implements ClientHttpRequestInterceptor {
    private final List<String> cookies;

    public CookieInterceptor(List<String> cookies) {
        this.cookies = cookies;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (!cookies.isEmpty()) {
            String cookieHeader = String.join("; ", cookies);
            request.getHeaders().add(HttpHeaders.COOKIE, cookieHeader);
        }
        return execution.execute(request, body);
    }
    
}