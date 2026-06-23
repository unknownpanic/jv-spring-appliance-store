package com.epam.rd.autocode.assessment.appliancestore.model.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CreateOrderRowRequestDto {
    @NotNull
    private Long applianceId;
    @NotNull
    @PositiveOrZero
    private Long quantity;
}
