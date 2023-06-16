package ru.team38.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.team38.common.dto.CaptchaDto;
import ru.team38.common.dto.LoginForm;
import ru.team38.userservice.exceptions.CaptchaCreationException;
import ru.team38.userservice.exceptions.LogoutFailedException;
import ru.team38.userservice.exceptions.UnauthorizedException;
import ru.team38.userservice.services.AuthService;
import ru.team38.userservice.services.CaptchaService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CaptchaService captchaService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginForm loginForm) {
        try {
            authService.getConnection(loginForm);
            return ResponseEntity.ok().body("Уcпешная аутентификация");
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            throw ex;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        try {
            authService.breakConnection();
            return ResponseEntity.ok().body("Успешный логаут");
        } catch (UnauthorizedException | LogoutFailedException ex) {
            throw ex;
        }
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
