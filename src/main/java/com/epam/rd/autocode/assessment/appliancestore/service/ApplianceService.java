package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.CreateApplianceRequestDto;
import java.util.List;

public interface ApplianceService {
    List<ApplianceResponseDto> getAll();

    ApplianceResponseDto getById(Long id);

    ApplianceResponseDto create(CreateApplianceRequestDto requestDto);

    ApplianceResponseDto updateById(Long id, CreateApplianceRequestDto requestDto);

    void deleteById(Long id);
}
