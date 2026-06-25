package com.epam.rd.autocode.assessment.appliancestore.model.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request for updating employee information")
public class UpdateEmployeeRequestDto {

    @Schema(
            description = "Employee full name",
            example = "John Smith"
    )
    @NotBlank
    private String name;

    @Schema(
            description = "Employee email address",
            example = "john.smith@company.com"
    )
    @Email
    private String email;

    @Schema(
            description = "Employee department",
            example = "Customer Support"
    )
    @NotBlank
    private String department;
}
