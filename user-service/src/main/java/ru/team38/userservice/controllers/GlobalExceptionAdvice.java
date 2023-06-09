package ru.team38.userservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.team38.userservice.exceptions.AccountRegister.AccountExistExcepton;
import ru.team38.userservice.exceptions.AccountRegister.AccountRegisterException;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(AccountRegisterException.class)
    public ResponseEntity<String> accountRegisterHandler(Exception ex) {
        ex.printStackTrace();
        if (ex.getCause() instanceof AccountExistExcepton) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
    }
}
