package com.epam.rd.autocode.assessment.appliancestore.model.dto.employee;

import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.FieldsMatch;
import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.StrongPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request for creating a new employee")
@FieldsMatch(
        field = "password",
        matchField = "repeatPassword",
        message = "Passwords don't match. Please re-enter your password."
)
public class CreateEmployeeRequestDto {

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
    @NotBlank
    @Email
    private String email;

    @Schema(
            description = """
                    Strong password containing uppercase, lowercase,
                    digit and special character
                    """,
            example = "Password123!"
    )
    @NotBlank
    @StrongPassword
    private String password;

    @Schema(
            description = "Password confirmation",
            example = "Password123!"
    )
    @NotBlank
    private String repeatPassword;

    @Schema(
            description = "Employee department",
            example = "Sales"
    )
    @NotBlank
    private String department;
}
