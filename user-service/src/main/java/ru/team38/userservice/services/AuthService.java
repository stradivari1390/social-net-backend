package ru.team38.userservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.team38.userservice.data.dto.LoginForm;
import ru.team38.userservice.MockUserBase;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MockUserBase mockUserBase;

    private AtomicBoolean login;

    {
        login = new AtomicBoolean();
    }

    public boolean getConnection(LoginForm loginForm) {
        login.set(true);
        return mockUserBase.isValidUser(loginForm);
    }

    public boolean breakConnection() {
        login.set(false);
        return true;
    }

    public boolean getLogin() {
        return login.get();
    }
}
