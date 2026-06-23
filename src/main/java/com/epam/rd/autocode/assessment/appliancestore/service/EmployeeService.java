package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.CreateEmployeeRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.EmployeeResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.UpdateEmployeeRequestDto;
import java.util.List;

public interface EmployeeService {
    List<EmployeeResponseDto> getAll();

    EmployeeResponseDto getById(Long id);

    EmployeeResponseDto create(CreateEmployeeRequestDto requestDto);

    EmployeeResponseDto updateById(Long id, UpdateEmployeeRequestDto requestDto);

    void deleteById(Long id);
}
