package com.epam.rd.autocode.assessment.appliancestore.model.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.Data;

@Data
@Schema(description = "Request for creating an order")
public class CreateOrderRequestDto {

    @Schema(
            description = "Order items",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotEmpty
    private Set<@Valid CreateOrderRowRequestDto> rows;
}
