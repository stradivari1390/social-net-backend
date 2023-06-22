package ru.team38.gatewayservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.team38.common.dto.AccountDto;
import ru.team38.common.dto.CaptchaDto;
import ru.team38.common.dto.LoginForm;


@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {

    @PostMapping("/api/v1/auth/login")
    ResponseEntity<String> login(@RequestBody LoginForm loginForm);

    @PostMapping("/api/v1/auth/logout")
    ResponseEntity<String> logout();

    @GetMapping("/api/v1/auth/captcha")
    ResponseEntity<CaptchaDto> getCaptcha();

    @GetMapping("/api/v1/friends/count")
    ResponseEntity<Integer> getIncomingFriendRequestsCount();

    @GetMapping("/api/v1/account/me")
    ResponseEntity<AccountDto> getAccount();
}