package com.epam.rd.autocode.assessment.appliancestore.model.dto.client;

import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.Card;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request for updating client information")
public class UpdateClientRequestDto {

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
    @Email
    private String email;

    @Schema(
            description = "Client payment card number",
            example = "1234-5678-9012-3456"
    )
    @Card
    private String card;
}
