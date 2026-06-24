package com.epam.rd.autocode.assessment.appliancestore.model.dto.client;

import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.Card;
import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.FieldsMatch;
import com.epam.rd.autocode.assessment.appliancestore.validation.annotation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@FieldsMatch(
        field = "password",
        matchField = "repeatPassword",
        message = "Passwords don't match. Please re-enter your password."
)
public class CreateClientRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @StrongPassword
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    @Card
    private String card;
}
