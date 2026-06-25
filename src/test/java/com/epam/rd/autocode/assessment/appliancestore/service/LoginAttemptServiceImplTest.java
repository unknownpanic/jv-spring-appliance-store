package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.security.LoginAttemptServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginAttemptServiceImplTest {
    private LoginAttemptServiceImpl loginAttemptService;
    private final String testEmail = "user@test.com";

    @BeforeEach
    void setUp() {
        loginAttemptService = new LoginAttemptServiceImpl();
    }

    @Test
    void isBlocked_ShouldReturnFalse_Initially() {
        assertFalse(loginAttemptService.isBlocked(testEmail));
    }

    @Test
    void isBlocked_ShouldReturnTrue_AfterFiveFailedAttempts() {
        for (int i = 0; i < 5; i++) {
            loginAttemptService.loginFailed(testEmail);
        }

        assertTrue(loginAttemptService.isBlocked(testEmail));
    }

    @Test
    void isBlocked_ShouldReturnFalse_AfterSuccessfulLogin() {
        loginAttemptService.loginFailed(testEmail);
        loginAttemptService.loginFailed(testEmail);
        loginAttemptService.loginFailed(testEmail);

        loginAttemptService.loginSucceeded(testEmail);

        assertFalse(loginAttemptService.isBlocked(testEmail));
    }

    @Test
    void loginFailed_ShouldIncrementCounterCorrectly() {
        loginAttemptService.loginFailed(testEmail);
        loginAttemptService.loginFailed(testEmail);

        assertFalse(loginAttemptService.isBlocked(testEmail));

        loginAttemptService.loginFailed(testEmail);
        loginAttemptService.loginFailed(testEmail);
        loginAttemptService.loginFailed(testEmail);

        assertTrue(loginAttemptService.isBlocked(testEmail));
    }
}
