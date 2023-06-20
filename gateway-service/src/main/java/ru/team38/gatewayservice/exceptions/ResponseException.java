package ru.team38.gatewayservice.exceptions;

import feign.Response;
import lombok.Getter;

public class ResponseException extends Exception {
    @Getter
    private final Response response;

    public ResponseException(String message, Response response) {
        super(message);
        this.response = response;
    }
}
