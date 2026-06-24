package com.epam.rd.autocode.assessment.appliancestore.mapper;

import com.epam.rd.autocode.assessment.appliancestore.config.MapperConfig;
import com.epam.rd.autocode.assessment.appliancestore.model.Employee;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.CreateEmployeeRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.EmployeeResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.UpdateEmployeeRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface EmployeeMapper {
    EmployeeResponseDto toDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Employee toModel(CreateEmployeeRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEmployee(@MappingTarget Employee employee, UpdateEmployeeRequestDto requestDto);
}
