package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceSearchParametersDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.CreateApplianceRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.service.ApplianceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
        name = "Appliance management",
        description = "Endpoints for managing appliances"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/appliances")
public class ApplianceController {
    private final ApplianceService applianceService;

    @Operation(
            summary = "Get all appliances",
            description = "Returns a paginated list of all appliances"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appliances retrieved successfully"
            )
    })
    @GetMapping
    public Page<ApplianceResponseDto> getAllAppliances(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return applianceService.getAll(pageable);
    }

    @Operation(
            summary = "Get appliance by ID",
            description = "Returns appliance details by identifier"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appliance found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appliance not found"
            )
    })
    @GetMapping("/{id}")
    public ApplianceResponseDto getApplianceById(
            @Parameter(description = "Appliance ID", example = "1")
            @PathVariable Long id
    ) {
        return applianceService.getById(id);
    }

    @Operation(
            summary = "Search appliances",
            description = "Search appliances by names and categories"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully"
            )
    })
    @GetMapping("/search")
    public Page<ApplianceResponseDto> searchAppliances(
            @Parameter(description = "Search parameters")
            ApplianceSearchParametersDto searchParameters,
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return applianceService.search(searchParameters, pageable);
    }

    @Operation(
            summary = "Create appliance",
            description = "Creates a new appliance. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Appliance created successfully"
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
    @PreAuthorize("hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplianceResponseDto createAppliance(
            @RequestBody @Valid CreateApplianceRequestDto requestDto
    ) {
        return applianceService.create(requestDto);
    }

    @Operation(
            summary = "Update appliance",
            description = "Updates an existing appliance. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Appliance updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appliance not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ApplianceResponseDto updateAppliance(
            @Parameter(description = "Appliance ID", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid CreateApplianceRequestDto requestDto
    ) {
        return applianceService.updateById(id, requestDto);
    }

    @Operation(
            summary = "Delete appliance",
            description = "Deletes an appliance by ID. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Appliance deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appliance not found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppliance(
            @Parameter(description = "Appliance ID", example = "1")
            @PathVariable Long id
    ) {
        applianceService.deleteById(id);
    }
}
