package ru.team38.userservice.exceptions;

public class CaptchaCreationException extends RuntimeException {
    public CaptchaCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}