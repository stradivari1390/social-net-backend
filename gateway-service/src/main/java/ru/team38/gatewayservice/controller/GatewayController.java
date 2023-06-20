package ru.team38.gatewayservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team38.common.dto.CaptchaDto;
import ru.team38.common.dto.LoginForm;
import ru.team38.gatewayservice.clients.UserServiceClient;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class GatewayController {

    private final UserServiceClient userServiceClient;

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginForm loginForm) {
        return userServiceClient.login(loginForm);
    }

    @PostMapping("/api/v1/auth/logout")
    public String logout() {
        return userServiceClient.logout().getBody();
    }

    @GetMapping("/api/v1/auth/captcha")
    public CaptchaDto getCaptcha() {
        return userServiceClient.getCaptcha().getBody();
    }

    @GetMapping("/api/v1/account/me")
    public String getAccount() {
        return userServiceClient.getAccount().getBody();
    }
}