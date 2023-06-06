package ru.team38.gatewayservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "user-service", url = "http://109.184.122.25:8081")
public interface UserServiceClient {

    @GetMapping("/user-service-endpoint")
    ResponseEntity<String> userEndpoint();
}