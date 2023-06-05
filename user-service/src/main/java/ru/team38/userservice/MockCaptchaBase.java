package ru.team38.userservice;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class MockCaptchaBase {
    private final ConcurrentHashMap<String, String> captchaBase;

    public MockCaptchaBase() {
        this.captchaBase = new ConcurrentHashMap<>();
    }

    public void storeCaptcha(String id, String solution) {
        captchaBase.put(id, solution);
    }

    public boolean isValidCaptcha(String id, String solution) {
        String storedSolution = captchaBase.get(id);
        return storedSolution != null && storedSolution.equals(solution);
    }
}