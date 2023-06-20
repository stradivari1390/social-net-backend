package ru.team38.gatewayservice.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.team38.gatewayservice.exceptions.ResponseException;

@Configuration
public class FeignConfig {

    @Autowired
    private HttpServletRequest request;

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor() {
        return requestTemplate -> {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    requestTemplate.header("Cookie", cookie.getName().concat("=").concat(cookie.getValue()));
                    //if ("token".equals(cookie.getName())) {
                    //    requestTemplate.header("Authorization", "Bearer " + cookie.getValue());
                    //}
                }
            }
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (msg, response) -> new ResponseException(msg, response);
    }

}