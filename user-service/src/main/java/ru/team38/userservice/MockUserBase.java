package ru.team38.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.team38.userservice.dto.LoginForm;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class MockUserBase {
    HashMap<String, String> userBase = new HashMap<>();

    {
        userBase.put("test", "test");
    }

    public boolean isValidUser(LoginForm loginForm) {
        boolean checkUser = userBase.containsKey(loginForm.getEmail())
                && userBase.get(loginForm.getEmail()).equals(loginForm.getPassword());
        return checkUser;
    }
}
