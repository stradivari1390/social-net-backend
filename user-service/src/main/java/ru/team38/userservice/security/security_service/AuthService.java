package ru.team38.userservice.security.security_service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Record;
import org.jooq.Result;
import org.mapstruct.factory.Mappers;
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
import ru.team38.common.dto.LoginForm;
import ru.team38.common.dto.RegisterDto;
import ru.team38.common.jooq.tables.records.AccountRecord;
import ru.team38.userservice.data.mappers.AccountMapper;
import ru.team38.userservice.data.repositories.AccountRepository;
import ru.team38.userservice.exceptions.AccountExistException;
import ru.team38.userservice.exceptions.AccountRegisterException;
import ru.team38.userservice.exceptions.LogoutFailedException;
import ru.team38.userservice.exceptions.PasswordMismatchException;
import ru.team38.userservice.exceptions.status.UnauthorizedException;
import ru.team38.userservice.security.jwt.JwtService;
import ru.team38.userservice.security.jwt.TokenBlacklistService;

import javax.security.auth.login.FailedLoginException;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final PasswordEncoder encoder;
    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);
    private final AccountRepository accountRepository;
    private final UserDetailsService userDetailsService;
    private final SecurityContextLogoutHandler logoutHandler;

    @Value("#{${jwt.cookie-max-age-min} * 60}")
    private int cookieMaxAge;

    @Transactional
    public void register(
            RegisterDto registerDto,
            HttpServletResponse response
    ) throws AccountRegisterException {
        if (!registerDto.getPassword1().equals(registerDto.getPassword2())) {
            throw new AccountRegisterException("Passwords mismatch", new PasswordMismatchException());
        }
        Result<Record> result = accountRepository.getAllAccountsByEmail(registerDto.getEmail());
        if (!result.isEmpty()) {
            throw new AccountRegisterException("User exists", new AccountExistException());
        }
        AccountRecord newAccountRecord =
                mapper.registerDto2AccountRecord(registerDto, encoder.encode(registerDto.getPassword1()));
        accountRepository.save(newAccountRecord);

        UserDetails userDetails = new User(
                newAccountRecord.getEmail(),
                newAccountRecord.getPassword(),
                true,
                true,
                true,
                newAccountRecord.getIsBlocked(),
                new ArrayList<>()
        );
        issueToken(userDetails, response);
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