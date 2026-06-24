package com.epam.rd.autocode.assessment.appliancestore.model.dto.client;

import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.Card;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateClientRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Card
    private String card;
}
