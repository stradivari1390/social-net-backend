package ru.team38.userservice.exceptions;

public class AccountRegisterException extends RuntimeException {
    public AccountRegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}
