package com.epam.rd.autocode.assessment.appliancestore.model.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Order item request")
public class CreateOrderRowRequestDto {

    @Schema(
            description = "Appliance identifier",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Positive
    private Long applianceId;

    @Schema(
            description = "Quantity of appliances",
            example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Positive
    private Long quantity;
}
