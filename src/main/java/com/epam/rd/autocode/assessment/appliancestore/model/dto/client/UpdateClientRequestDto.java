package com.epam.rd.autocode.assessment.appliancestore.model.dto.client;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateClientRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String card;
}
