package com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request for creating a manufacturer")
public class CreateManufacturerRequestDto {

    @Schema(
            description = "Manufacturer name",
            example = "Samsung",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank
    private String name;
}
