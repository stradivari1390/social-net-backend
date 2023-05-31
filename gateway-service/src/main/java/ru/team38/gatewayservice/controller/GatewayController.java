package ru.team38.gatewayservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.team38.gatewayservice.clients.CommunicationsServiceClient;
import ru.team38.gatewayservice.clients.UserServiceClient;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    private final UserServiceClient userServiceClient;
    private final CommunicationsServiceClient communicationsServiceClient;

    @Autowired
    public GatewayController(UserServiceClient userServiceClient,
                             CommunicationsServiceClient communicationsServiceClient) {
        this.userServiceClient = userServiceClient;
        this.communicationsServiceClient = communicationsServiceClient;
    }

    @GetMapping("/user-service-endpoint")
    public ResponseEntity<String> getUserEndpoint() {
        return userServiceClient.userEndpoint();
    }

    @GetMapping("/communications-service-endpoint")
    public ResponseEntity<String> getCommunicationsEndpoint() {
        return communicationsServiceClient.communicationsEndpoint();
    }
}