package ru.team38.gatewayservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.team38.common.dto.LoginForm;
import ru.team38.gatewayservice.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginForm loginForm) {
        log.info("Executing login request");
        return userService.login(loginForm);
    }

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<String> logout() {
        log.info("Executing logout request");
        return userService.logout();
    }

    @GetMapping("/api/v1/auth/captcha")
    public ResponseEntity<?> getCaptcha() {
        log.info("Executing getCaptcha request");
        return userService.getCaptcha();
    }

    @GetMapping("/api/v1/friends/count")
    public ResponseEntity<?> getIncomingFriendRequests() {
        log.info("Executing getIncomingFriendRequests request");
        return userService.getIncomingFriendRequests();
    }

    @GetMapping("/api/v1/account/me")
    public ResponseEntity<?> getAccount() {
        log.info("Executing getAccount request");
        return userService.getAccount();
    }
}