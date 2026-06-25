package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.ClientResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.CreateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.UpdateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Client management",
        description = "Endpoints for managing client profiles"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    @Operation(
            summary = "Get all clients",
            description = "Returns a list of all clients. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Clients retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<ClientResponseDto> getAll() {
        return clientService.getAll();
    }

    @Operation(
            summary = "Get my profile",
            description = "Returns the profile of the currently authenticated client."
                    + " CLIENT role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found"
            )
    })
    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENT')")
    public ClientResponseDto getMyProfile(
            @Parameter(hidden = true) Authentication authentication
    ) {
        return clientService.getByEmail(authentication.getName());
    }

    @Operation(
            summary = "Get client by ID",
            description = "Returns client details by identifier. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Client found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found"
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ClientResponseDto getById(
            @Parameter(description = "Client ID", example = "1")
            @PathVariable Long id
    ) {
        return clientService.getById(id);
    }

    @Operation(
            summary = "Create client",
            description = "Creates a new client manually. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Client created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ClientResponseDto create(
            @RequestBody @Valid CreateClientRequestDto requestDto
    ) {
        return clientService.create(requestDto);
    }

    @Operation(
            summary = "Update client",
            description = "Updates an existing client by ID. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Client updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found"
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ClientResponseDto update(
            @Parameter(description = "Client ID", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid UpdateClientRequestDto requestDto
    ) {
        return clientService.updateById(id, requestDto);
    }

    @Operation(
            summary = "Update my profile",
            description = "Updates the profile of the currently authenticated client. "
                    + "CLIENT role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found"
            )
    })
    @PutMapping("/me")
    @PreAuthorize("hasRole('CLIENT')")
    public ClientResponseDto updateMyProfile(
            @Parameter(hidden = true) Authentication authentication,
            @RequestBody @Valid UpdateClientRequestDto requestDto
    ) {
        return clientService.updateByEmail(authentication.getName(), requestDto);
    }

    @Operation(
            summary = "Delete client",
            description = "Deletes a client by ID. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Client deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found"
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Client ID", example = "1")
            @PathVariable Long id
    ) {
        clientService.deleteById(id);
    }
}
