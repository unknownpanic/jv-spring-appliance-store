package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.CreateEmployeeRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.EmployeeResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.UpdateEmployeeRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.service.EmployeeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<EmployeeResponseDto> getAll() {
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public EmployeeResponseDto getById(@PathVariable Long id) {
        return employeeService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponseDto create(
            @RequestBody @Valid CreateEmployeeRequestDto requestDto
    ) {
        return employeeService.create(requestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public EmployeeResponseDto update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateEmployeeRequestDto requestDto
    ) {
        return employeeService.updateById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        employeeService.deleteById(id);
    }
}
