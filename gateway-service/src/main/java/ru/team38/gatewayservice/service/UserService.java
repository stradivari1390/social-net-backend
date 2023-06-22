package ru.team38.gatewayservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.team38.common.dto.AccountDto;
import ru.team38.common.dto.CaptchaDto;
import ru.team38.common.dto.LoginForm;
import ru.team38.common.dto.RegisterDto;
import ru.team38.gatewayservice.clients.UserServiceClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserServiceClient userServiceClient;

    public ResponseEntity<String> register(RegisterDto registerDto) {
        try {
            return userServiceClient.register(registerDto);
        } catch (FeignException e) {
            log.error(e.contentUTF8());
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }

    public ResponseEntity<String> login(LoginForm loginForm) {
        try {
            return userServiceClient.login(loginForm);
        } catch (FeignException e) {
            log.error(e.contentUTF8());
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }

    public ResponseEntity<String> logout() {
        try {
            return userServiceClient.logout();
        } catch (FeignException e) {
            log.error(e.contentUTF8());
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }

    public CaptchaDto getCaptcha() {
        try {
            ResponseEntity<CaptchaDto> responseEntity = userServiceClient.getCaptcha();
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                throw new RuntimeException("Failed to get captcha");
            }
        } catch (FeignException e) {
            throw new RuntimeException(e.contentUTF8(), e);
        }
    }

    public Integer getIncomingFriendRequestsCount() {
        try {
            ResponseEntity<Integer> responseEntity = userServiceClient.getIncomingFriendRequestsCount();
            return responseEntity.getBody();
        } catch (FeignException e) {
            log.error(e.contentUTF8(), e);
            throw new RuntimeException(e.contentUTF8(), e);
        }
    }

    public AccountDto getAccount() {
        try {
            ResponseEntity<AccountDto> responseEntity = userServiceClient.getAccount();
            return responseEntity.getBody();
        } catch (FeignException e) {
            log.error(e.contentUTF8());
            throw new RuntimeException(e.contentUTF8(), e);
        }
    }
}