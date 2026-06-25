package com.epam.rd.autocode.assessment.appliancestore.model.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Employee information")
public class EmployeeResponseDto {

    @Schema(
            description = "Employee identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Employee full name",
            example = "John Smith"
    )
    private String name;

    @Schema(
            description = "Employee email address",
            example = "john.smith@company.com"
    )
    private String email;

    @Schema(
            description = "Employee department",
            example = "Sales"
    )
    private String department;
}
