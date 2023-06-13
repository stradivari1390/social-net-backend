package ru.team38.userservice.exceptions;

public class AccountExistException extends Exception {
    public AccountExistException() {
        super("User exist", null);
    }
}
