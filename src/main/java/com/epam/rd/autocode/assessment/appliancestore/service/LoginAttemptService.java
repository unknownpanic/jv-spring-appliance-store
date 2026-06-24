package com.epam.rd.autocode.assessment.appliancestore.service;

public interface LoginAttemptService {
    void loginFailed(String email);

    void loginSucceeded(String email);

    boolean isBlocked(String email);
}
