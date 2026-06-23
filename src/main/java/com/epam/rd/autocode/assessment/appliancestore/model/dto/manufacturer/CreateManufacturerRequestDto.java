package com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateManufacturerRequestDto {
    @NotBlank
    private String name;
}
