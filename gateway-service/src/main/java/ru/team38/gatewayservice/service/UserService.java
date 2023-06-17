package ru.team38.gatewayservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.team38.common.dto.LoginForm;
import ru.team38.gatewayservice.clients.UserServiceClient;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserServiceClient userServiceClient;

    public ResponseEntity<String> login(LoginForm loginForm) {
        try {
            return userServiceClient.login(loginForm);
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }

    public ResponseEntity<String> logout() {
        try {
            return userServiceClient.logout();
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }

    public ResponseEntity<?> getCaptcha() {
        try {
            return userServiceClient.getCaptcha();
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }

    public ResponseEntity<?> getIncomingFriendRequests() {
        try {
            return userServiceClient.getIncomingFriendRequests();
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }

    public ResponseEntity<?> getAccount() {
        try {
            return userServiceClient.getAccount();
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }
}