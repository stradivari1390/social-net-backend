package ru.team38.userservice.exceptions;

public class FriendsServiceException extends RuntimeException {
    public FriendsServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}