package com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateApplianceRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @NotBlank
    private String model;
    @NotNull
    @Positive
    private Long manufacturerId;
    @NotBlank
    private String powerType;
    private String characteristic;
    private String description;
    @NotNull
    @PositiveOrZero
    private Integer power;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
}
