package com.epam.rd.autocode.assessment.appliancestore.model.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateOrderRowRequestDto {
    @NotNull
    @Positive
    private Long applianceId;
    @NotNull
    @Positive
    private Long quantity;
}
