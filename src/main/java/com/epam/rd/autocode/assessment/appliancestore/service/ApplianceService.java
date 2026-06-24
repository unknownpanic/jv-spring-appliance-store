package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceSearchParametersDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.CreateApplianceRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplianceService {
    Page<ApplianceResponseDto> getAll(Pageable pageable);

    ApplianceResponseDto getById(Long id);

    Page<ApplianceResponseDto> search(
            ApplianceSearchParametersDto searchParameters,
            Pageable pageable);

    ApplianceResponseDto create(CreateApplianceRequestDto requestDto);

    ApplianceResponseDto updateById(Long id, CreateApplianceRequestDto requestDto);

    void deleteById(Long id);
}
