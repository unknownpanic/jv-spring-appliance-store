package com.epam.rd.autocode.assessment.appliancestore.model.dto.client;

import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.Card;
import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.FieldsMatch;
import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.StrongPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request for creating a new client")
@FieldsMatch(
        field = "password",
        matchField = "repeatPassword",
        message = "Passwords don't match. Please re-enter your password."
)
public class CreateClientRequestDto {

    @Schema(
            description = "Client full name",
            example = "John Doe"
    )
    @NotBlank
    private String name;

    @Schema(
            description = "Client email address",
            example = "john.doe@example.com"
    )
    @NotBlank
    @Email
    private String email;

    @Schema(
            description = """
                    Password must satisfy strong password requirements:
                    uppercase letter, lowercase letter, digit and special character.
                    """,
            example = "Password!123"
    )
    @NotBlank
    @StrongPassword
    private String password;

    @Schema(
            description = "Password confirmation",
            example = "Password!123"
    )
    @NotBlank
    private String repeatPassword;

    @Schema(
            description = "Client payment card number",
            example = "1234-5678-9012-3456"
    )
    @Card
    private String card;
}
