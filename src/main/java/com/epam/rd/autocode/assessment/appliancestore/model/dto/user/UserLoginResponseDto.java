package com.epam.rd.autocode.assessment.appliancestore.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "JWT authentication response")
public class UserLoginResponseDto {

    @Schema(
            description = "JWT access token",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSJ9"
    )
    private String token;
}
