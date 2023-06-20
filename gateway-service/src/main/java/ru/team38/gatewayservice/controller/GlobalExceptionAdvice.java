package ru.team38.gatewayservice.controller;

import feign.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.team38.gatewayservice.exceptions.ResponseException;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<String> responseExceprionHandler(ResponseException ex) {
        ex.printStackTrace();
        Response response = ex.getResponse();
        return ResponseEntity.status(response.status())
                .body(response.body() == null ? "" : response.body().toString());
    }
}
