package ru.team38.userservice.security.security_controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.team38.userservice.security.security_services.AuthenticationService;
import ru.team38.userservice.security.security_services.CustomUserDetailsService;
import ru.team38.userservice.security.security_services.TokenBlacklistService;
import ru.team38.userservice.security.jwt.JWTUtil;
import ru.team38.userservice.security.jwt.JwtRequest;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AuthUserController {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenBlacklistService tokenBlacklistService;
    private final AuthenticationService authenticationService;

    @GetMapping("/signin")
    public String handleSignIn(Model model) {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp() {
        return "redirect:http://user-service/signup";
    }

    @GetMapping("/profile")
    public String handleProfile(Principal principal) {
        if (principal != null) {
            return "redirect:http://user-service/profile";
        } else {
            return "redirect:/signin";
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest,
                                                       HttpServletResponse response) throws Exception {
        authenticationService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setMaxAge(60 * 60 * 24);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public String handleLogout(HttpServletRequest request) {
        String token = authenticationService.extractTokenFromRequest(request);
        tokenBlacklistService.addToBlacklist(token);    // ToDo: add all blacklisted tokens to redis
        return "redirect:/signin";
    }
}
