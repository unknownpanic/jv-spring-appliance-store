package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.ClientResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.CreateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.user.UserLoginRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.user.UserLoginResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.security.AuthenticationService;
import com.epam.rd.autocode.assessment.appliancestore.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Authentication management",
        description = "Endpoints for user login and registration"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ClientService clientService;

    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully authenticated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credentials or request data"
            )
    })
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @Operation(
            summary = "Register client",
            description = "Registers a new user with the CLIENT role"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Client registered successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            )
    })
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponseDto register(@RequestBody @Valid CreateClientRequestDto requestDto) {
        return clientService.create(requestDto);
    }
}
