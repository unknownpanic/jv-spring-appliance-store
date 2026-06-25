package com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Schema(description = "Appliance response details")
public class ApplianceResponseDto {

    @Schema(
            description = "Unique identifier of the appliance",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Appliance name",
            example = "Washing Machine"
    )
    private String name;

    @Schema(
            description = "Appliance category",
            example = "Large Home Appliances"
    )
    private String category;

    @Schema(
            description = "Appliance model",
            example = "WM-2000X"
    )
    private String model;

    @Schema(
            description = "Identifier of the manufacturer",
            example = "1"
    )
    private String manufacturerId;

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

    @Schema(
            description = "Power consumption in Watts",
            example = "2000"
    )
    private Integer power;

    @Schema(
            description = "Price of the appliance",
            example = "15499.50"
    )
    private BigDecimal price;
}
