package ru.team38.userservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.team38.userservice.exceptions.AccountExistException;
import ru.team38.userservice.exceptions.AccountRegisterException;
import ru.team38.userservice.exceptions.LogoutFailedException;
import ru.team38.userservice.exceptions.UnauthorizedException;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(AccountRegisterException.class)
    public ResponseEntity<String> accountRegisterHandler(Exception ex) {
        ex.printStackTrace();
        if (ex.getCause() instanceof AccountExistException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> userNotFoundHandler(UsernameNotFoundException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account does not exist.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badCredentialsHandler(BadCredentialsException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> unauthorizedHandler(UnauthorizedException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in.");
    }

    @ExceptionHandler(LogoutFailedException.class)
    public ResponseEntity<String> logoutFailedHandler(LogoutFailedException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Logout failed.");
    }
}
