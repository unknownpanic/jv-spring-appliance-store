package com.epam.rd.autocode.assessment.appliancestore.model.dto.client;

import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.Card;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateClientRequestDto {
    @NotBlank
    private String name;
    @Email
    private String email;
    @Card
    private String card;
}
