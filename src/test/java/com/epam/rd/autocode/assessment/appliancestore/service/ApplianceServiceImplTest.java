package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.exception.EntityNotFoundException;
import com.epam.rd.autocode.assessment.appliancestore.mapper.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliancestore.model.Appliance;
import com.epam.rd.autocode.assessment.appliancestore.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceSearchParametersDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.CreateApplianceRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliancestore.repository.SpecificationBuilder;
import com.epam.rd.autocode.assessment.appliancestore.repository.appliance.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliancestore.service.impl.ApplianceServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplianceServiceImplTest {
    @Mock
    private SpecificationBuilder<Appliance, ApplianceSearchParametersDto> applianceSpecificationBuilder;
    @Mock
    private ApplianceRepository applianceRepository;
    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private ApplianceMapper applianceMapper;
    @InjectMocks
    private ApplianceServiceImpl applianceService;
    private Appliance appliance;
    private ApplianceResponseDto applianceResponseDto;
    private CreateApplianceRequestDto createRequestDto;
    private Manufacturer manufacturer;

    @BeforeEach
    void setUp() {
        manufacturer = new Manufacturer();
        manufacturer.setId(1L);
        manufacturer.setName("Test Manufacturer");

        appliance = new Appliance();
        appliance.setId(10L);
        appliance.setModel("Test Model");
        appliance.setManufacturer(manufacturer);

        applianceResponseDto = new ApplianceResponseDto();
        applianceResponseDto.setId(10L);
        applianceResponseDto.setModel("Test Model");

        createRequestDto = new CreateApplianceRequestDto();
        createRequestDto.setManufacturerId(1L);
        createRequestDto.setModel("Test Model");
        createRequestDto.setCategory("BIG");
        createRequestDto.setPowerType("AC220");
    }

    @Test
    void getAll_ShouldReturnPageOfAppliances() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Appliance> appliancePage = new PageImpl<>(List.of(appliance));

        when(applianceRepository.findAll(pageable)).thenReturn(appliancePage);
        when(applianceMapper.toDto(appliance)).thenReturn(applianceResponseDto);

        Page<ApplianceResponseDto> result = applianceService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Model", result.getContent().get(0).getModel());
        verify(applianceRepository).findAll(pageable);
    }

    @Test
    void getById_ShouldReturnAppliance_WhenExists() {
        when(applianceRepository.findById(10L)).thenReturn(Optional.of(appliance));
        when(applianceMapper.toDto(appliance)).thenReturn(applianceResponseDto);

        ApplianceResponseDto result = applianceService.getById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(applianceRepository).findById(10L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(applianceRepository.findById(10L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> applianceService.getById(10L));

        assertEquals("Appliance not found", exception.getMessage());
        verify(applianceRepository).findById(10L);
    }

    @Test
    void search_ShouldReturnFilteredAppliances() {
        ApplianceSearchParametersDto searchParams = new ApplianceSearchParametersDto();
        Pageable pageable = PageRequest.of(0, 10);
        Specification<Appliance> spec = mock(Specification.class);
        Page<Appliance> appliancePage = new PageImpl<>(List.of(appliance));

        when(applianceSpecificationBuilder.build(searchParams)).thenReturn(spec);
        when(applianceRepository.findAll(spec, pageable)).thenReturn(appliancePage);
        when(applianceMapper.toDto(appliance)).thenReturn(applianceResponseDto);

        Page<ApplianceResponseDto> result = applianceService.search(searchParams, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(applianceSpecificationBuilder).build(searchParams);
        verify(applianceRepository).findAll(spec, pageable);
    }

    @Test
    void create_ShouldCreateAndReturnAppliance() {
        when(applianceMapper.toModel(createRequestDto)).thenReturn(appliance);
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));
        when(applianceRepository.save(appliance)).thenReturn(appliance);
        when(applianceMapper.toDto(appliance)).thenReturn(applianceResponseDto);

        ApplianceResponseDto result = applianceService.create(createRequestDto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(manufacturerRepository).findById(1L);
        verify(applianceRepository).save(appliance);
    }

    @Test
    void create_ShouldThrowException_WhenManufacturerNotFound() {
        when(applianceMapper.toModel(createRequestDto)).thenReturn(appliance);
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> applianceService.create(createRequestDto));

        assertEquals("Manufacturer not found", exception.getMessage());
        verify(applianceRepository, never()).save(any());
    }

    @Test
    void updateById_ShouldUpdateAndReturnAppliance() {
        when(applianceRepository.findById(10L)).thenReturn(Optional.of(appliance));
        doNothing().when(applianceMapper).updateAppliance(appliance, createRequestDto);
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));
        when(applianceRepository.save(appliance)).thenReturn(appliance);
        when(applianceMapper.toDto(appliance)).thenReturn(applianceResponseDto);

        ApplianceResponseDto result = applianceService.updateById(10L, createRequestDto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(applianceRepository).findById(10L);
        verify(applianceMapper).updateAppliance(appliance, createRequestDto);
        verify(applianceRepository).save(appliance);
    }

    @Test
    void updateById_ShouldThrowException_WhenApplianceNotFound() {
        when(applianceRepository.findById(10L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> applianceService.updateById(10L, createRequestDto));

        assertEquals("Appliance not found", exception.getMessage());
        verify(applianceMapper, never()).updateAppliance(any(), any());
        verify(applianceRepository, never()).save(any());
    }

    @Test
    void deleteById_ShouldThrowException_WhenDoesNotExist() {
        when(applianceRepository.existsById(99L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> applianceService.deleteById(99L));
    }
}
