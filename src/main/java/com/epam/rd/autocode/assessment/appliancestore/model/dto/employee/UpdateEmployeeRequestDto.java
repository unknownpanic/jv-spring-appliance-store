package com.epam.rd.autocode.assessment.appliancestore.model.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateEmployeeRequestDto {
    @NotBlank
    private String name;
    @Email
    private String email;
    @NotBlank
    private String department;
}
