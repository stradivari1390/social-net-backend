package ru.team38.userservice.exceptions.status;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {super();}

    public UnauthorizedException(String message) {
        super(message);
    }
}
