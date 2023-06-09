package ru.team38.userservice.exceptions.AccountRegister;

public class PasswordMismatchException extends Exception {
    public PasswordMismatchException() {
        super("Password mismatch", null);
    }
}
