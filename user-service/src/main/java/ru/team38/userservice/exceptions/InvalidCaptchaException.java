package ru.team38.userservice.exceptions;

public class InvalidCaptchaException extends RuntimeException {
    public InvalidCaptchaException(String message) {
        super(message);
    }
}
