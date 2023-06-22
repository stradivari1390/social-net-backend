package ru.team38.gatewayservice.controller;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> responseExceprionHandler(FeignException ex) {
        ex.printStackTrace();
        log.error(ex.contentUTF8());
        return ResponseEntity.status(ex.status()).body(ex.contentUTF8());
    }
}
