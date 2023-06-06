package ru.team38.userservice.security.jwt;

import ch.qos.logback.classic.Logger;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtTokenUtil;
    private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    public JwtAuthenticationFilter(JWTUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    try {
                        username = jwtTokenUtil.getUsernameFromToken(token);
                    } catch (ExpiredJwtException e) {
                        httpServletResponse.sendRedirect("/signin");
                        return;
                    }
                }
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = jwtTokenUtil.getUserDetails(token);
                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        log.debug("Username: {}, Token: {}", username, token);
                        log.debug("Authentication before setting SecurityContextHolder: {}", SecurityContextHolder.getContext().getAuthentication());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        log.debug("Authentication after setting SecurityContextHolder: {}", SecurityContextHolder.getContext().getAuthentication());
                    } else {
                        log.debug("Username: {}, Token: {}", username, token);
                        log.debug("Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
                        httpServletResponse.sendRedirect("/logout");
                        return;
                    }
                }
            }
        }
        if (httpServletRequest.getRequestURI().equals("/signup")) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }   // ToDO: Refactor method!
}