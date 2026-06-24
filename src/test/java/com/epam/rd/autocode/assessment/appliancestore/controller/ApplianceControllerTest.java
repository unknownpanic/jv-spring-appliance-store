package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.exception.GlobalExceptionHandler;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceSearchParametersDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.CreateApplianceRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.service.ApplianceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
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
class ApplianceControllerTest {
    @Mock
    private ApplianceService applianceService;
    @InjectMocks
    private ApplianceController applianceController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(applianceController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                 .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllAppliances_ShouldReturnPageOfAppliances() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<ApplianceResponseDto> appliances = Arrays.asList(
                createApplianceResponseDto(1L, "Refrigerator", "Home Appliances", "RX-100", "1", "Electric", "Energy efficient", "Large capacity", 200, BigDecimal.valueOf(599.99)),
                createApplianceResponseDto(2L, "Washing Machine", "Home Appliances", "WM-200", "2", "Electric", "Smart", "Quick wash", 150, BigDecimal.valueOf(399.99))
        );
        Page<ApplianceResponseDto> page = new PageImpl<>(appliances, pageable, appliances.size());

        when(applianceService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/appliances")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Refrigerator"))
                .andExpect(jsonPath("$.content[0].category").value("Home Appliances"))
                .andExpect(jsonPath("$.content[0].model").value("RX-100"))
                .andExpect(jsonPath("$.content[0].price").value(599.99))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("Washing Machine"))
                .andExpect(jsonPath("$.content[1].category").value("Home Appliances"));

        verify(applianceService).getAll(any(Pageable.class));
    }

    @Test
    void getApplianceById_ShouldReturnAppliance() throws Exception {
        Long id = 1L;
        ApplianceResponseDto appliance = createApplianceResponseDto(
                id,
                "Microwave",
                "Kitchen Appliances",
                "MW-300",
                "3",
                "Electric",
                "Digital display",
                "30L capacity",
                100,
                BigDecimal.valueOf(149.99)
        );

        when(applianceService.getById(id)).thenReturn(appliance);

        mockMvc.perform(get("/appliances/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Microwave"))
                .andExpect(jsonPath("$.category").value("Kitchen Appliances"))
                .andExpect(jsonPath("$.model").value("MW-300"))
                .andExpect(jsonPath("$.manufacturerId").value("3"))
                .andExpect(jsonPath("$.powerType").value("Electric"))
                .andExpect(jsonPath("$.price").value(149.99));

        verify(applianceService).getById(id);
    }

    @Test
    void searchAppliances_ShouldReturnFilteredPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<ApplianceResponseDto> appliances = Arrays.asList(
                createApplianceResponseDto(1L, "Samsung TV", "Electronics", "ST-500", "4", "Electric", "Smart TV", "55 inch", 200, BigDecimal.valueOf(999.99))
        );
        Page<ApplianceResponseDto> page = new PageImpl<>(appliances, pageable, appliances.size());

        when(applianceService.search(any(ApplianceSearchParametersDto.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/appliances/search")
                        .param("names", "Samsung TV")
                        .param("categories", "Electronics")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Samsung TV"))
                .andExpect(jsonPath("$.content[0].category").value("Electronics"));

        verify(applianceService).search(any(ApplianceSearchParametersDto.class), any(Pageable.class));
    }

    @Test
    void searchAppliances_WithMultipleNamesAndCategories_ShouldReturnFilteredPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<ApplianceResponseDto> appliances = Arrays.asList(
                createApplianceResponseDto(1L, "LG TV", "Electronics", "LG-500", "5", "Electric", "Smart TV", "65 inch", 200, BigDecimal.valueOf(899.99)),
                createApplianceResponseDto(2L, "Samsung TV", "Electronics", "ST-500", "4", "Electric", "Smart TV", "55 inch", 200, BigDecimal.valueOf(999.99))
        );
        Page<ApplianceResponseDto> page = new PageImpl<>(appliances, pageable, appliances.size());

        when(applianceService.search(any(ApplianceSearchParametersDto.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/appliances/search")
                        .param("names", "LG TV")
                        .param("names", "Samsung TV")
                        .param("categories", "Electronics")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(applianceService).search(any(ApplianceSearchParametersDto.class), any(Pageable.class));
    }

    @Test
    void createAppliance_ShouldReturnCreatedAppliance() throws Exception {
        CreateApplianceRequestDto requestDto = createCreateApplianceRequestDto(
                "Air Conditioner",
                "Home Appliances",
                "AC-400",
                6L,
                "Electric",
                "Inverter technology",
                "1.5 ton capacity",
                150,
                BigDecimal.valueOf(799.99)
        );

        ApplianceResponseDto responseDto = createApplianceResponseDto(
                1L,
                "Air Conditioner",
                "Home Appliances",
                "AC-400",
                "6",
                "Electric",
                "Inverter technology",
                "1.5 ton capacity",
                150,
                BigDecimal.valueOf(799.99)
        );

        when(applianceService.create(any(CreateApplianceRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/appliances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Air Conditioner"))
                .andExpect(jsonPath("$.category").value("Home Appliances"))
                .andExpect(jsonPath("$.model").value("AC-400"))
                .andExpect(jsonPath("$.manufacturerId").value("6"))
                .andExpect(jsonPath("$.powerType").value("Electric"))
                .andExpect(jsonPath("$.characteristic").value("Inverter technology"))
                .andExpect(jsonPath("$.description").value("1.5 ton capacity"))
                .andExpect(jsonPath("$.power").value(150))
                .andExpect(jsonPath("$.price").value(799.99));

        verify(applianceService).create(any(CreateApplianceRequestDto.class));
    }

    @Test
    void createAppliance_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CreateApplianceRequestDto invalidRequest = new CreateApplianceRequestDto();

        mockMvc.perform(post("/appliances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAppliance_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        CreateApplianceRequestDto invalidRequest = createCreateApplianceRequestDto(
                "",
                "Home Appliances",
                "AC-400",
                6L,
                "Electric",
                "Inverter technology",
                "1.5 ton capacity",
                150,
                BigDecimal.valueOf(799.99)
        );

        mockMvc.perform(post("/appliances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAppliance_WithNegativePrice_ShouldReturnBadRequest() throws Exception {
        CreateApplianceRequestDto invalidRequest = createCreateApplianceRequestDto(
                "Air Conditioner",
                "Home Appliances",
                "AC-400",
                6L,
                "Electric",
                "Inverter technology",
                "1.5 ton capacity",
                150,
                BigDecimal.valueOf(-100.00)
        );

        mockMvc.perform(post("/appliances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAppliance_ShouldReturnUpdatedAppliance() throws Exception {
        Long id = 1L;
        CreateApplianceRequestDto requestDto = createCreateApplianceRequestDto(
                "Updated Refrigerator",
                "Kitchen Appliances",
                "RF-200",
                2L,
                "Electric",
                "Energy efficient",
                "Large capacity",
                180,
                BigDecimal.valueOf(699.99)
        );

        ApplianceResponseDto responseDto = createApplianceResponseDto(
                id,
                "Updated Refrigerator",
                "Kitchen Appliances",
                "RF-200",
                "2",
                "Electric",
                "Energy efficient",
                "Large capacity",
                180,
                BigDecimal.valueOf(699.99)
        );

        when(applianceService.updateById(eq(id), any(CreateApplianceRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/appliances/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Updated Refrigerator"))
                .andExpect(jsonPath("$.category").value("Kitchen Appliances"))
                .andExpect(jsonPath("$.model").value("RF-200"))
                .andExpect(jsonPath("$.manufacturerId").value("2"))
                .andExpect(jsonPath("$.powerType").value("Electric"))
                .andExpect(jsonPath("$.characteristic").value("Energy efficient"))
                .andExpect(jsonPath("$.description").value("Large capacity"))
                .andExpect(jsonPath("$.power").value(180))
                .andExpect(jsonPath("$.price").value(699.99));

        verify(applianceService).updateById(eq(id), any(CreateApplianceRequestDto.class));
    }

    @Test
    void updateAppliance_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Long id = 1L;
        CreateApplianceRequestDto invalidRequest = new CreateApplianceRequestDto();

        mockMvc.perform(put("/appliances/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAppliance_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(applianceService).deleteById(id);

        mockMvc.perform(delete("/appliances/{id}", id))
                .andExpect(status().isNoContent());

        verify(applianceService).deleteById(id);
    }

    @Test
    void getAllAppliances_WithCustomPageable_ShouldReturnCorrectPage() throws Exception {
        Pageable pageable = PageRequest.of(1, 5);
        List<ApplianceResponseDto> appliances = Arrays.asList(
                createApplianceResponseDto(6L, "Dishwasher", "Home Appliances", "DW-300", "7", "Electric", "Water efficient", "12 place settings", 200, BigDecimal.valueOf(499.99))
        );
        Page<ApplianceResponseDto> page = new PageImpl<>(appliances, pageable, 10);

        when(applianceService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/appliances")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "name,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.content[0].name").value("Dishwasher"));

        verify(applianceService).getAll(any(Pageable.class));
    }

    @Test
    void searchAppliances_WithEmptySearchParameters_ShouldReturnAllResults() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<ApplianceResponseDto> appliances = Arrays.asList(
                createApplianceResponseDto(1L, "Refrigerator", "Home Appliances", "RX-100", "1", "Electric", "Energy efficient", "Large capacity", 200, BigDecimal.valueOf(599.99)),
                createApplianceResponseDto(2L, "Washing Machine", "Home Appliances", "WM-200", "2", "Electric", "Smart", "Quick wash", 150, BigDecimal.valueOf(399.99))
        );
        Page<ApplianceResponseDto> page = new PageImpl<>(appliances, pageable, appliances.size());

        when(applianceService.search(any(ApplianceSearchParametersDto.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/appliances/search")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(applianceService).search(any(ApplianceSearchParametersDto.class), any(Pageable.class));
    }



    private ApplianceResponseDto createApplianceResponseDto(
            Long id,
            String name,
            String category,
            String model,
            String manufacturerId,
            String powerType,
            String characteristic,
            String description,
            Integer power,
            BigDecimal price) {

        ApplianceResponseDto dto = new ApplianceResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setCategory(category);
        dto.setModel(model);
        dto.setManufacturerId(manufacturerId);
        dto.setPowerType(powerType);
        dto.setCharacteristic(characteristic);
        dto.setDescription(description);
        dto.setPower(power);
        dto.setPrice(price);
        return dto;
    }

    private CreateApplianceRequestDto createCreateApplianceRequestDto(
            String name,
            String category,
            String model,
            Long manufacturerId,
            String powerType,
            String characteristic,
            String description,
            Integer power,
            BigDecimal price) {

        CreateApplianceRequestDto dto = new CreateApplianceRequestDto();
        dto.setName(name);
        dto.setCategory(category);
        dto.setModel(model);
        dto.setManufacturerId(manufacturerId);
        dto.setPowerType(powerType);
        dto.setCharacteristic(characteristic);
        dto.setDescription(description);
        dto.setPower(power);
        dto.setPrice(price);
        return dto;
    }
}
