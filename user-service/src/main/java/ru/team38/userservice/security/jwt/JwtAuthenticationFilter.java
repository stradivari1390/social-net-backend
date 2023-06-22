package ru.team38.userservice.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Value("#{${jwt.cookie-max-age-min} * 60}")
    private int cookieMaxAge;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getTokenFromHeader(request);

            if (token != null) {
                createCookieIfNotPresent(request, response, token);
            } else {
                token = getTokenFromCookies(request);
                if (token == null) return;
            }

            String email = getEmailFromToken(token, request, response);
            if (email == null) return;

            authenticateIfNecessary(request, email, token);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private void createCookieIfNotPresent(HttpServletRequest request,
                                          HttpServletResponse response, String token) {
        Cookie tokenCookie = findCookie(request, "token");
        if (tokenCookie == null && !jwtService.isTokenExpired(token)) {
                tokenCookie = new Cookie("token", token);
                tokenCookie.setMaxAge(cookieMaxAge);
                tokenCookie.setHttpOnly(true);
                tokenCookie.setPath("/");
                response.addCookie(tokenCookie);
                log.info("Created new token cookie.");
        }
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getTokenFromCookies(HttpServletRequest request) {
        Cookie tokenCookie = findCookie(request, "token");
        if (tokenCookie != null) {
            String token = tokenCookie.getValue();
            if (!jwtService.isTokenExpired(token)) {
                return token;
            }
        }
        return null;
    }

    private void invalidateCookie(HttpServletResponse response, Cookie cookie) {
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        log.info("Invalidated token cookie.");
    }

    private String getEmailFromToken(String token, HttpServletRequest request, HttpServletResponse response) {
        try {
            return jwtService.getUsername(token);
        } catch (ExpiredJwtException e) {
            Cookie tokenCookie = findCookie(request, "token");
            if (tokenCookie != null) {
                invalidateCookie(response, tokenCookie);
            }
            log.error("Failed to get email from token.", e);
            return null;
        }
    }

    private void authenticateIfNecessary(HttpServletRequest request, String email, String token) {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Authenticated user: {}", email);
            }
        }
    }

    private Cookie findCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
}