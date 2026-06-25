package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.exception.EntityNotFoundException;
import com.epam.rd.autocode.assessment.appliancestore.mapper.ManufacturerMapper;
import com.epam.rd.autocode.assessment.appliancestore.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.CreateManufacturerRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.ManufacturerResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliancestore.service.impl.ManufacturerServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManufacturerServiceImplTest {
    @Mock
    private ManufacturerRepository manufacturerRepository;
    @Mock
    private ManufacturerMapper manufacturerMapper;
    @InjectMocks
    private ManufacturerServiceImpl manufacturerService;
    private Manufacturer manufacturer;
    private ManufacturerResponseDto responseDto;
    private CreateManufacturerRequestDto requestDto;

    @BeforeEach
    void setUp() {
        manufacturer = new Manufacturer();
        manufacturer.setId(1L);
        manufacturer.setName("Samsung");

        responseDto = new ManufacturerResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Samsung");

        requestDto = new CreateManufacturerRequestDto();
        requestDto.setName("Samsung");
    }

    @Test
    void getAll_ShouldReturnListOfManufacturers() {
        when(manufacturerRepository.findAll()).thenReturn(List.of(manufacturer));
        when(manufacturerMapper.toDto(manufacturer)).thenReturn(responseDto);

        List<ManufacturerResponseDto> result = manufacturerService.getAll();

        assertEquals(1, result.size());
        assertEquals("Samsung", result.get(0).getName());
        verify(manufacturerRepository).findAll();
    }

    @Test
    void getById_ShouldReturnManufacturer_WhenExists() {
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));
        when(manufacturerMapper.toDto(manufacturer)).thenReturn(responseDto);

        ManufacturerResponseDto result = manufacturerService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(manufacturerRepository).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> manufacturerService.getById(1L));
    }

    @Test
    void create_ShouldReturnCreatedManufacturer() {
        when(manufacturerMapper.toModel(requestDto)).thenReturn(manufacturer);
        when(manufacturerRepository.save(manufacturer)).thenReturn(manufacturer);
        when(manufacturerMapper.toDto(manufacturer)).thenReturn(responseDto);

        ManufacturerResponseDto result = manufacturerService.create(requestDto);

        assertEquals("Samsung", result.getName());
        verify(manufacturerRepository).save(manufacturer);
    }

    @Test
    void updateById_ShouldReturnUpdatedManufacturer() {
        when(manufacturerRepository.findById(1L)).thenReturn(Optional.of(manufacturer));
        when(manufacturerRepository.save(manufacturer)).thenReturn(manufacturer);
        when(manufacturerMapper.toDto(manufacturer)).thenReturn(responseDto);

        ManufacturerResponseDto result = manufacturerService.updateById(1L, requestDto);

        assertNotNull(result);
        verify(manufacturerMapper).updateManufacturer(manufacturer, requestDto);
        verify(manufacturerRepository).save(manufacturer);
    }

    @Test
    void deleteById_ShouldThrowException_WhenDoesNotExist() {
        when(manufacturerRepository.existsById(99L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> manufacturerService.deleteById(99L));
    }
}
