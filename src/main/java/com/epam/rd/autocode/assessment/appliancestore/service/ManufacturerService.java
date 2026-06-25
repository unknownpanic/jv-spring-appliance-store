package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.CreateManufacturerRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.ManufacturerResponseDto;
import java.util.List;

public interface ManufacturerService {
    List<ManufacturerResponseDto> getAll();

    ManufacturerResponseDto getById(Long id);

    ManufacturerResponseDto create(CreateManufacturerRequestDto requestDto);

    ManufacturerResponseDto updateById(Long id, CreateManufacturerRequestDto requestDto);

    void deleteById(Long id);
}
