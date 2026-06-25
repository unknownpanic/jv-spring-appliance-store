package com.epam.rd.autocode.assessment.appliancestore.model.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Client information")
public class ClientResponseDto {

    @Schema(
            description = "Client identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Client full name",
            example = "John Doe"
    )
    private String name;

    @Schema(
            description = "Client email address",
            example = "john.doe@example.com"
    )
    private String email;
}
