package ru.team38.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team38.userservice.dto.CaptchaDto;
import ru.team38.userservice.dto.LoginForm;
import ru.team38.userservice.services.AuthService;
import ru.team38.userservice.services.CaptchaService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CaptchaService captchaService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginForm loginForm) {

        JSONObject response = new JSONObject();

        if (!authService.getConnection(loginForm)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        response.put("result", "Уcпешная аутентификация");
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        JSONObject response = new JSONObject();

        if (!authService.getLogin()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!authService.breakConnection()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        response.put("result", "Успешный логаут");
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaDto> getCaptcha() {
        if (!authService.getLogin()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            CaptchaDto captcha = captchaService.createCaptcha();
            return ResponseEntity.ok().body(captcha);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
