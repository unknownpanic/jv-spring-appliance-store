package com.epam.rd.autocode.assessment.appliancestore.model.dto.employee;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateEmployeeRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String department;
}
