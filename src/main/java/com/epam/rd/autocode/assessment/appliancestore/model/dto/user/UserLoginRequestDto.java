package com.epam.rd.autocode.assessment.appliancestore.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "User authentication request")
public class UserLoginRequestDto {

    @Schema(
            description = "User email address",
            example = "john.doe@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    @Email
    private String email;

    @Schema(
            description = "User password",
            example = "Password123!",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String password;
}
