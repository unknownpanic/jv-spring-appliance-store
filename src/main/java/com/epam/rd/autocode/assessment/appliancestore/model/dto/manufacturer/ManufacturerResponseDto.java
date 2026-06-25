package com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Manufacturer information")
public class ManufacturerResponseDto {

    @Schema(
            description = "Manufacturer identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Manufacturer name",
            example = "Samsung"
    )
    private String name;
}
