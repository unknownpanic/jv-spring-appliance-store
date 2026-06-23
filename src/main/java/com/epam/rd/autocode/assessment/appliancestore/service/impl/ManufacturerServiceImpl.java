package com.epam.rd.autocode.assessment.appliancestore.service.impl;

import com.epam.rd.autocode.assessment.appliancestore.mapper.ManufacturerMapper;
import com.epam.rd.autocode.assessment.appliancestore.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.CreateManufacturerRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.ManufacturerResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliancestore.service.ManufacturerService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManufacturerServiceImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerMapper manufacturerMapper;

    public List<ManufacturerResponseDto> getAll() {
        return manufacturerRepository.findAll().stream()
                .map(manufacturerMapper::toDto)
                .toList();
    }

    @Override
    public ManufacturerResponseDto getById(Long id) {
        return manufacturerMapper.toDto(manufacturerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Manufacturer not found")));
    }

    @Transactional
    public ManufacturerResponseDto create(CreateManufacturerRequestDto requestDto) {
        Manufacturer manufacturer = manufacturerMapper.toModel(requestDto);
        return manufacturerMapper.toDto(manufacturerRepository.save(manufacturer));
    }

    @Transactional
    public ManufacturerResponseDto updateById(Long id, CreateManufacturerRequestDto requestDto) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer not found"));
        manufacturerMapper.updateManufacturer(manufacturer, requestDto);
        return manufacturerMapper.toDto(manufacturerRepository.save(manufacturer));
    }

    @Transactional
    public void deleteById(Long id) {
        manufacturerRepository.deleteById(id);
    }
}
