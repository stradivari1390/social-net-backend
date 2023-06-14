package ru.team38.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.team38.common.dto.LoginForm;
import ru.team38.common.dto.RegisterDto;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MockUserBase {

    Map<String, String> userBase = new HashMap<>();

    {
        userBase.put("test", "test");
    }

    public boolean isValidUser(LoginForm loginForm) {
        return userBase.containsKey(loginForm.getEmail())
                && userBase.get(loginForm.getEmail()).equals(loginForm.getPassword());
    }

    public boolean addAccount(RegisterDto account) {
        final String key = account.getEmail();
        if (userBase.containsKey(key)) {
            return false;
        }
        userBase.put(key, account.toString());
        return true;
    }

    public Map<String, String> getUserBase() {
        return userBase;
    }
}
