package com.epam.rd.autocode.assessment.appliancestore.service.impl;

import com.epam.rd.autocode.assessment.appliancestore.service.LoginAttemptService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();

    @Override
    public void loginFailed(String email) {
        attemptsCache.put(email, attemptsCache.getOrDefault(email, 0) + 1);
    }

    @Override
    public void loginSucceeded(String email) {
        attemptsCache.remove(email);
    }

    @Override
    public boolean isBlocked(String email) {
        return attemptsCache.getOrDefault(email, 0) >= MAX_ATTEMPTS;
    }
}
