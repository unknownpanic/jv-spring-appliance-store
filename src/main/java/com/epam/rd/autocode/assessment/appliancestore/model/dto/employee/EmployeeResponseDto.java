package com.epam.rd.autocode.assessment.appliancestore.model.dto.employee;

import lombok.Data;

@Data
public class EmployeeResponseDto {
    private Long id;
    private String name;
    private String email;
    private String department;
}
