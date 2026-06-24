package com.epam.rd.autocode.assessment.appliancestore.service.impl;

import com.epam.rd.autocode.assessment.appliancestore.exception.EntityNotFoundException;
import com.epam.rd.autocode.assessment.appliancestore.mapper.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliancestore.model.Appliance;
import com.epam.rd.autocode.assessment.appliancestore.model.Category;
import com.epam.rd.autocode.assessment.appliancestore.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliancestore.model.PowerType;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceSearchParametersDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.CreateApplianceRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliancestore.repository.SpecificationBuilder;
import com.epam.rd.autocode.assessment.appliancestore.repository.appliance.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliancestore.service.ApplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplianceServiceImpl implements ApplianceService {
    private final SpecificationBuilder<Appliance, ApplianceSearchParametersDto>
            applianceSpecificationBuilder;
    private final ApplianceRepository applianceRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ApplianceMapper applianceMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ApplianceResponseDto> getAll(Pageable pageable) {
        return applianceRepository.findAll(pageable)
                .map(applianceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplianceResponseDto getById(Long id) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appliance not found"));
        return applianceMapper.toDto(appliance);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplianceResponseDto> search(
            ApplianceSearchParametersDto searchParameters,
            Pageable pageable
    ) {
        Specification<Appliance> applianceSpecification =
                applianceSpecificationBuilder.build(searchParameters);

        return applianceRepository.findAll(applianceSpecification, pageable)
                .map(applianceMapper::toDto);
    }

    @Override
    @Transactional
    public ApplianceResponseDto create(CreateApplianceRequestDto requestDto) {
        Appliance appliance = applianceMapper.toModel(requestDto);
        setEnumsAndManufacturer(appliance, requestDto);
        return applianceMapper.toDto(applianceRepository.save(appliance));
    }

    @Override
    @Transactional
    public ApplianceResponseDto updateById(Long id, CreateApplianceRequestDto requestDto) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appliance not found"));
        applianceMapper.updateAppliance(appliance, requestDto);
        setEnumsAndManufacturer(appliance, requestDto);
        return applianceMapper.toDto(applianceRepository.save(appliance));
    }

    @Override
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
