package ru.team38.userservice.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.team38.common.dto.AccountDto;
import ru.team38.common.dto.LoginForm;
import ru.team38.common.dto.RegisterDto;
import ru.team38.userservice.data.repositories.AccountRepository;
import ru.team38.userservice.exceptions.LogoutFailedException;
import ru.team38.userservice.exceptions.status.BadRequestException;
import ru.team38.userservice.exceptions.status.UnauthorizedException;
import ru.team38.userservice.security.jwt.JwtService;
import ru.team38.userservice.security.jwt.TokenBlacklistService;

import javax.security.auth.login.FailedLoginException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final PasswordEncoder encoder;
    private final AccountRepository accountRepository;
    private final UserDetailsService userDetailsService;
    private final SecurityContextLogoutHandler logoutHandler;

    @Value("#{${jwt.cookie-max-age-min} * 60}")
    private int cookieMaxAge;

    @Transactional
    public void register(RegisterDto registerDto, HttpServletResponse response) {
        try {
            log.info("Executing registration request");
            if (!registerDto.getPassword1().equals(registerDto.getPassword2())) {
                throw new BadRequestException("Passwords mismatch");
            }
            accountRepository.getAccountByEmail(registerDto.getEmail()).ifPresent(record -> {
                throw new UnauthorizedException("User exists");
            });
            AccountDto newAccount = AccountDto.builder().email(registerDto.getEmail())
                    .password(encoder.encode(registerDto.getPassword1()))
                    .firstName(registerDto.getFirstName()).lastName(registerDto.getLastName())
                    .isDeleted(false).isOnline(false).isBlocked(false).createdOn(ZonedDateTime.now())
                    .messagePermission(true).regDate(ZonedDateTime.now()).birthDate(LocalDate.now())
                    .build();
            accountRepository.save(newAccount);
            issueToken(newUserDetails(newAccount), response);
        }
        catch (Throwable e) {
            log.error("Error executing registration request", e);
            throw new BadRequestException("");
        }
    }

    public void login(LoginForm loginForm,
                      HttpServletResponse response) throws UsernameNotFoundException, BadCredentialsException, FailedLoginException {
        log.info("Executing login request");
        try {
            if (isUserLoggedIn()) {
                throw new FailedLoginException("User already logged in");
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginForm.getEmail());
            boolean isValidPassword = BCrypt.checkpw(loginForm.getPassword(), userDetails.getPassword());
            if (!isValidPassword) {
                throw new BadCredentialsException("Invalid password");
            }
            issueToken(userDetails, response);
        } catch (FailedLoginException | BadCredentialsException e) {
            log.error("Error executing login request", e);
            throw e;
        }
    }

    public void logout(Authentication authentication, HttpServletRequest request,
                       HttpServletResponse response) throws LogoutFailedException {
        log.info("Executing logout request");
        if (!isUserLoggedIn()) {
            throw new UnauthorizedException("User is not logged in");
        }
        try {
            logoutHandler.logout(request, response, authentication);
            disableToken(request, response);
            SecurityContextHolder.clearContext();
            if (isUserLoggedIn()) {
                throw new LogoutFailedException("Logout failed");
            }
        } catch (UnauthorizedException | LogoutFailedException e) {
            log.error("Error executing logout request", e);
            throw new LogoutFailedException("Error executing logout request");
        }
    }

    public boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private UserDetails newUserDetails(AccountDto accountDto){
        return new User(accountDto.getEmail(), accountDto.getPassword(), true,
                true, true, accountDto.getIsBlocked(), new ArrayList<>());
    }

    public void issueToken(UserDetails userDetails, HttpServletResponse httpServletResponse) {
        final String token = jwtService.generateToken(userDetails);
        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setMaxAge(cookieMaxAge);
        jwtCookie.setPath("/");
        httpServletResponse.addCookie(jwtCookie);
    }

    public void disableToken(HttpServletRequest request, HttpServletResponse response) {
        String token = null;
        Cookie cookieToDelete = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    cookieToDelete = new Cookie(cookie.getName(), null);
                    String contextPath = request.getContextPath();
                    String cookiePath = StringUtils.hasText(contextPath) ? contextPath : "/";
                    cookieToDelete.setPath(cookiePath);
                    cookieToDelete.setMaxAge(0);
                    cookieToDelete.setSecure(request.isSecure());
                    break;
                }
            }
        }
        if (token != null) {
            tokenBlacklistService.addTokenToBlacklist(token);
            response.addCookie(cookieToDelete);
        }
    }
}