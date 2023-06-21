package ru.team38.userservice.exceptions;

public class DatabaseQueryException extends RuntimeException {
    public DatabaseQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}