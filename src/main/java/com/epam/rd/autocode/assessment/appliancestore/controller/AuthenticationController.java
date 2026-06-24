package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.user.UserLoginRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.user.UserLoginResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserLoginResponseDto login(
            @RequestBody @Valid UserLoginRequestDto requestDto
    ) {
        return authenticationService.authenticate(requestDto);
    }
}
