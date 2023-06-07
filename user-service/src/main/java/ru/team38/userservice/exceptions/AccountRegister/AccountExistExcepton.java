package ru.team38.userservice.exceptions.AccountRegister;

public class AccountExistExcepton extends Exception {
    public AccountExistExcepton() {
        super("User exist", null);
    }
}
