package ru.team38.userservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.team38.common.dto.CaptchaDto;
import ru.team38.common.dto.LoginForm;
import ru.team38.userservice.exceptions.CaptchaCreationException;
import ru.team38.userservice.exceptions.LogoutFailedException;
import ru.team38.common.dto.RegisterDto;
import ru.team38.userservice.exceptions.AccountRegisterException;
import ru.team38.userservice.services.AuthService;
import ru.team38.userservice.services.CaptchaService;

import javax.security.auth.login.FailedLoginException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CaptchaService captchaService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterDto registerDto,
            HttpServletResponse response
    ) throws AccountRegisterException {
        authService.register(registerDto, response);
        return ResponseEntity.ok("");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody @Valid LoginForm loginForm,
            HttpServletResponse response
    ) throws FailedLoginException {
        authService.login(loginForm, response);
        return ResponseEntity.ok().body("Уcпешная аутентификация");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws LogoutFailedException {
        authService.logout(authentication, request, response);
        return ResponseEntity.ok().body("Успешный логаут");
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaDto> getCaptcha() {
        try {
            CaptchaDto captcha = captchaService.createCaptcha();
            return ResponseEntity.ok().body(captcha);
        } catch (CaptchaCreationException e) {
            throw e;
        }
    }
}
