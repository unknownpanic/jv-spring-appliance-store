package com.epam.rd.autocode.assessment.appliancestore.service.impl;

import com.epam.rd.autocode.assessment.appliancestore.mapper.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliancestore.model.Appliance;
import com.epam.rd.autocode.assessment.appliancestore.model.Category;
import com.epam.rd.autocode.assessment.appliancestore.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliancestore.model.PowerType;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.CreateApplianceRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliancestore.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliancestore.service.ApplianceService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplianceServiceImpl implements ApplianceService {
    private final ApplianceRepository applianceRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ApplianceMapper applianceMapper;

    public List<ApplianceResponseDto> getAll() {
        return applianceRepository.findAll().stream()
                .map(applianceMapper::toDto)
                .toList();
    }

    public ApplianceResponseDto getById(Long id) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appliance not found"));
        return applianceMapper.toDto(appliance);
    }

    @Transactional
    public ApplianceResponseDto create(CreateApplianceRequestDto requestDto) {
        Appliance appliance = applianceMapper.toModel(requestDto);
        setEnumsAndManufacturer(appliance, requestDto);
        return applianceMapper.toDto(applianceRepository.save(appliance));
    }

    @Transactional
    public ApplianceResponseDto updateById(Long id, CreateApplianceRequestDto requestDto) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appliance not found"));
        applianceMapper.updateAppliance(appliance, requestDto);
        setEnumsAndManufacturer(appliance, requestDto);
        return applianceMapper.toDto(applianceRepository.save(appliance));
    }

    @Transactional
    public void deleteById(Long id) {
        applianceRepository.deleteById(id);
    }

    private void setEnumsAndManufacturer(
            Appliance appliance,
            CreateApplianceRequestDto requestDto
    ) {
        Manufacturer manufacturer = manufacturerRepository.findById(requestDto.getManufacturerId())
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer not found"));
        appliance.setManufacturer(manufacturer);
        appliance.setCategory(Category.valueOf(requestDto.getCategory().toUpperCase()));
        appliance.setPowerType(PowerType.valueOf(requestDto.getPowerType().toUpperCase()));
    }
}
