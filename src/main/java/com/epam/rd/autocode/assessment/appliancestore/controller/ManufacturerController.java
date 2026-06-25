package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.CreateManufacturerRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.ManufacturerResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.service.ManufacturerService;
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
        name = "Manufacturer management",
        description = "Endpoints for managing manufacturers"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/manufacturers")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @Operation(
            summary = "Get all manufacturers",
            description = "Returns a list of all manufacturers. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Manufacturers retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "403", description = "Access denied"
            )
    })
    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<ManufacturerResponseDto> getAllManufacturers() {
        return manufacturerService.getAll();
    }

    @Operation(
            summary = "Get manufacturer by ID",
            description = "Returns manufacturer details by identifier. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Manufacturer found"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Manufacturer not found"
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ManufacturerResponseDto getManufacturerById(
            @Parameter(description = "Manufacturer ID", example = "1")
            @PathVariable Long id
    ) {
        return manufacturerService.getById(id);
    }

    @Operation(
            summary = "Create manufacturer",
            description = "Creates a new manufacturer. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Manufacturer created successfully"
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
    public ManufacturerResponseDto createManufacturer(
            @RequestBody @Valid CreateManufacturerRequestDto requestDto
    ) {
        return manufacturerService.create(requestDto);
    }

    @Operation(
            summary = "Update manufacturer",
            description = "Updates an existing manufacturer by ID. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Manufacturer updated successfully"
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
                    description = "Manufacturer not found"
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ManufacturerResponseDto updateManufacturer(
            @Parameter(description = "Manufacturer ID", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid CreateManufacturerRequestDto requestDto
    ) {
        return manufacturerService.updateById(id, requestDto);
    }

    @Operation(
            summary = "Delete manufacturer",
            description = "Deletes a manufacturer by ID. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Manufacturer deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Manufacturer not found"
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteManufacturer(
            @Parameter(description = "Manufacturer ID", example = "1")
            @PathVariable Long id
    ) {
        manufacturerService.deleteById(id);
    }
}
