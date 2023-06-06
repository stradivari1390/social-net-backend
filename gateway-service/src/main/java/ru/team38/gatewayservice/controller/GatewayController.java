package ru.team38.gatewayservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.team38.gatewayservice.clients.CommunicationsServiceClient;
import ru.team38.gatewayservice.clients.UserServiceClient;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gateway")
public class GatewayController {

    private final UserServiceClient userServiceClient;
    private final CommunicationsServiceClient communicationsServiceClient;

    @GetMapping("/user-service-endpoint")
    public ResponseEntity<String> getUserEndpoint() {
        return userServiceClient.userEndpoint();
    }

    @GetMapping("/communications-service-endpoint")
    public ResponseEntity<String> getCommunicationsEndpoint() {
        return communicationsServiceClient.communicationsEndpoint();
    }
}