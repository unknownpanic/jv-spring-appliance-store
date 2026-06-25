package com.epam.rd.autocode.assessment.appliancestore.security;

import com.epam.rd.autocode.assessment.appliancestore.exception.AccountIsBlockedException;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.user.UserLoginRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.user.UserLoginResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;
    private final JwtUtil jwtUtil;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        if (loginAttemptService.isBlocked(requestDto.getEmail())) {
            throw new AccountIsBlockedException(
                    "Account is blocked due to too many failed attempts.");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(), requestDto.getPassword()
                    )
            );
            loginAttemptService.loginSucceeded(requestDto.getEmail());
            String token = jwtUtil.generateToken(authentication.getName(),
                    authentication.getAuthorities());
            log.info("User {} successfully authenticated", requestDto.getEmail());
            return new UserLoginResponseDto(token);
        } catch (BadCredentialsException ex) {
            loginAttemptService.loginFailed(requestDto.getEmail());
            throw ex;
        }
    }
}
