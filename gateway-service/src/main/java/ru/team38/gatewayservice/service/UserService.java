package ru.team38.gatewayservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.team38.common.dto.*;
import ru.team38.common.dto.notification.NotificationCountDto;
import ru.team38.gatewayservice.clients.UserServiceClient;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserServiceClient userServiceClient;

    public ResponseEntity<String> register(RegisterDto registerDto) {
        return userServiceClient.register(registerDto);
    }

    public LoginResponse login(LoginForm loginForm) {
        return userServiceClient.login(loginForm).getBody();
    }

    public ResponseEntity<String> logout() {
        return userServiceClient.logout();
    }

    public LoginResponse refresh(RefreshTokenRequest request) {
        return userServiceClient.refresh(request).getBody();
    }

    public CaptchaDto getCaptcha() {
        ResponseEntity<CaptchaDto> responseEntity = userServiceClient.getCaptcha();
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Failed to get captcha");
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

    public PageFriendShortDto getFriendsByParameters(FriendSearchDto friendSearchDto, PageDto pageDto) {
        return userServiceClient.getFriendsByParameters(friendSearchDto, pageDto).getBody();
    }

    public AccountDto getAccount() {
        return userServiceClient.getAccount().getBody();
    }

    public AccountDto updateAccount(AccountDto account) {
        return userServiceClient.updateAccount(account).getBody();
    }
    public AccountDto getAccountById(UUID id) {
        try {
            return userServiceClient.getAccountById(id).getBody();
        } catch (FeignException e) {
            log.error(e.contentUTF8());
            throw new RuntimeException(e.contentUTF8(), e);
        }
    }

    public NotificationCountDto getNotificationsCount() {
        return userServiceClient.getNotificationsCount().getBody();
    }

    public ResponseEntity<String> deleteAccount() {
        return userServiceClient.deleteAccount();
    }
}