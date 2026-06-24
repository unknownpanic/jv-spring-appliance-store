package com.epam.rd.autocode.assessment.appliancestore.service.impl;

import com.epam.rd.autocode.assessment.appliancestore.exception.EntityNotFoundException;
import com.epam.rd.autocode.assessment.appliancestore.mapper.EmployeeMapper;
import com.epam.rd.autocode.assessment.appliancestore.model.Employee;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.CreateEmployeeRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.EmployeeResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.UpdateEmployeeRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliancestore.service.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAll() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return employeeMapper.toDto(employee);
    }

    @Override
    @Transactional
    public EmployeeResponseDto create(CreateEmployeeRequestDto requestDto) {
        if (employeeRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
        Employee employee = employeeMapper.toModel(requestDto);
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeResponseDto updateById(Long id, UpdateEmployeeRequestDto requestDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employeeRepository.findByEmail(requestDto.getEmail())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(e -> {
                    throw new IllegalArgumentException("Email is already in use");
                });

        employeeMapper.updateEmployee(employee, requestDto);
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setDeleted(true);
        employeeRepository.save(employee);
    }
}
