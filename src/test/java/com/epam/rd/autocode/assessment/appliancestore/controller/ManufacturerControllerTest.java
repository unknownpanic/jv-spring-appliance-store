package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.exception.GlobalExceptionHandler;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.CreateManufacturerRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.ManufacturerResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.service.ManufacturerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ManufacturerControllerTest {
    @Mock
    private ManufacturerService manufacturerService;
    @InjectMocks
    private ManufacturerController manufacturerController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(manufacturerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllManufacturers_ShouldReturnListOfManufacturers() throws Exception {
        List<ManufacturerResponseDto> manufacturers = Arrays.asList(
                createManufacturerResponseDto(1L, "Samsung"),
                createManufacturerResponseDto(2L, "LG")
        );

        when(manufacturerService.getAll()).thenReturn(manufacturers);

        mockMvc.perform(get("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Samsung"))
                .andExpect(jsonPath("$[1].name").value("LG"));

        verify(manufacturerService).getAll();
    }

    @Test
    void getManufacturerById_ShouldReturnManufacturer() throws Exception {
        Long id = 1L;
        ManufacturerResponseDto responseDto = createManufacturerResponseDto(id, "Samsung");

        when(manufacturerService.getById(id)).thenReturn(responseDto);

        mockMvc.perform(get("/manufacturers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Samsung"));

        verify(manufacturerService).getById(id);
    }

    @Test
    void createManufacturer_ShouldReturnCreated() throws Exception {
        CreateManufacturerRequestDto requestDto = createCreateManufacturerRequestDto("Samsung");
        ManufacturerResponseDto responseDto = createManufacturerResponseDto(1L, "Samsung");

        when(manufacturerService.create(any(CreateManufacturerRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Samsung"));

        verify(manufacturerService).create(any(CreateManufacturerRequestDto.class));
    }

    @Test
    void createManufacturer_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CreateManufacturerRequestDto invalidRequest = new CreateManufacturerRequestDto();

        mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateManufacturer_ShouldReturnUpdated() throws Exception {
        Long id = 1L;
        CreateManufacturerRequestDto requestDto = createCreateManufacturerRequestDto("Updated Samsung");
        ManufacturerResponseDto responseDto = createManufacturerResponseDto(id, "Updated Samsung");

        when(manufacturerService.updateById(eq(id), any(CreateManufacturerRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/manufacturers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Updated Samsung"));

        verify(manufacturerService).updateById(eq(id), any(CreateManufacturerRequestDto.class));
    }

    @Test
    void deleteManufacturer_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(manufacturerService).deleteById(id);

        mockMvc.perform(delete("/manufacturers/{id}", id))
                .andExpect(status().isNoContent());

        verify(manufacturerService).deleteById(id);
    }

    private ManufacturerResponseDto createManufacturerResponseDto(Long id, String name) {
        ManufacturerResponseDto dto = new ManufacturerResponseDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }

    private CreateManufacturerRequestDto createCreateManufacturerRequestDto(String name) {
        CreateManufacturerRequestDto dto = new CreateManufacturerRequestDto();
        dto.setName(name);
        return dto;
    }
}
