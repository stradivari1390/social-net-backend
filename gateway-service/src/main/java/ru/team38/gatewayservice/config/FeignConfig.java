package ru.team38.gatewayservice.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final HttpServletRequest request;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        String jwtToken = cookie.getValue();
                        requestTemplate.header("Authorization", "Bearer " + jwtToken);
                    }
                }
            }
        };
    }
}