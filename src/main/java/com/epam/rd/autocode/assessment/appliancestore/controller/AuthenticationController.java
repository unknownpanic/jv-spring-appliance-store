package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.ClientResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.CreateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.user.UserLoginRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.user.UserLoginResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.security.AuthenticationService;
import com.epam.rd.autocode.assessment.appliancestore.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ClientService clientService;

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponseDto register(@RequestBody @Valid CreateClientRequestDto requestDto) {
        return clientService.create(requestDto);
    }
}
