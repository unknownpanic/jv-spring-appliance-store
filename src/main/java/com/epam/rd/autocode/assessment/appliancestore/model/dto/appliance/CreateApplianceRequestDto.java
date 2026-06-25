package com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Schema(description = "Request to create or update an appliance")
public class CreateApplianceRequestDto {

    @NotBlank
    @Schema(
            description = "Appliance name",
            example = "Washing Machine"
    )
    private String name;

    @NotBlank
    @Schema(
            description = "Appliance category",
            example = "Large Home Appliances"
    )
    private String category;

    @NotBlank
    @Schema(
            description = "Appliance model",
            example = "WM-2000X"
    )
    private String model;

    @NotNull
    @Positive
    @Schema(
            description = "Identifier of the manufacturer",
            example = "1"
    )
    private Long manufacturerId;

    @NotBlank
    @Schema(
            description = "Type of power supply",
            example = "220V AC"
    )
    private String powerType;

    @Schema(
            description = "Key characteristics of the appliance",
            example = "Capacity 8kg, 1200 RPM"
    )
    private String characteristic;

    @Schema(
            description = "Detailed description of the appliance",
            example = "Front-loading washing machine with an energy-efficient inverter motor"
    )
    private String description;

    @NotNull
    @PositiveOrZero
    @Schema(
            description = "Power consumption in Watts",
            example = "2000"
    )
    private Integer power;

    @NotNull
    @PositiveOrZero
    @Schema(
            description = "Price of the appliance",
            example = "15499.50"
    )
    private BigDecimal price;
}
