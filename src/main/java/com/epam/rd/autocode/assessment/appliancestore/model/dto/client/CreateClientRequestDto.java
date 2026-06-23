package com.epam.rd.autocode.assessment.appliancestore.model.dto.client;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateClientRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String card;
}
