package ru.team38.userservice.exceptions;

public class PasswordMismatchException extends Exception {
    public PasswordMismatchException() {
        super("Password mismatch", null);
    }
}
