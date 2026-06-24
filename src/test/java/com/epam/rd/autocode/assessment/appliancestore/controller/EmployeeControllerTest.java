package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.exception.GlobalExceptionHandler;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.CreateEmployeeRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.EmployeeResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.UpdateEmployeeRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.service.EmployeeService;
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
class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private EmployeeController employeeController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAll_ShouldReturnListOfEmployees() throws Exception {
        List<EmployeeResponseDto> employees = Arrays.asList(
                createEmployeeResponseDto(1L, "John Doe", "john@company.com", "IT"),
                createEmployeeResponseDto(2L, "Jane Smith", "jane@company.com", "HR")
        );

        when(employeeService.getAll()).thenReturn(employees);

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(employeeService).getAll();
    }

    @Test
    void getById_ShouldReturnEmployee() throws Exception {
        Long id = 1L;
        EmployeeResponseDto employee = createEmployeeResponseDto(id, "John Doe", "john@company.com", "IT");

        when(employeeService.getById(id)).thenReturn(employee);

        mockMvc.perform(get("/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(employeeService).getById(id);
    }

    @Test
    void create_ShouldReturnCreatedEmployee() throws Exception {
        CreateEmployeeRequestDto requestDto = createCreateEmployeeRequestDto(
                "John Doe", "john@company.com", "Password123!", "Password123!", "IT"
        );
        EmployeeResponseDto responseDto = createEmployeeResponseDto(1L, "John Doe", "john@company.com", "IT");

        when(employeeService.create(any(CreateEmployeeRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(employeeService).create(any(CreateEmployeeRequestDto.class));
    }

    @Test
    void create_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CreateEmployeeRequestDto invalidRequest = new CreateEmployeeRequestDto();

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_ShouldReturnUpdatedEmployee() throws Exception {
        Long id = 1L;
        UpdateEmployeeRequestDto requestDto = createUpdateEmployeeRequestDto("John Updated", "john.new@company.com", "Management");
        EmployeeResponseDto responseDto = createEmployeeResponseDto(id, "John Updated", "john.new@company.com", "Management");

        when(employeeService.updateById(eq(id), any(UpdateEmployeeRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.department").value("Management"));

        verify(employeeService).updateById(eq(id), any(UpdateEmployeeRequestDto.class));
    }

    @Test
    void update_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Long id = 1L;
        UpdateEmployeeRequestDto invalidRequest = new UpdateEmployeeRequestDto();

        mockMvc.perform(put("/employees/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(employeeService).deleteById(id);

        mockMvc.perform(delete("/employees/{id}", id))
                .andExpect(status().isNoContent());

        verify(employeeService).deleteById(id);
    }

    private EmployeeResponseDto createEmployeeResponseDto(Long id, String name, String email, String department) {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        dto.setDepartment(department);
        return dto;
    }

    private CreateEmployeeRequestDto createCreateEmployeeRequestDto(
            String name, String email, String password, String repeatPassword, String department) {
        CreateEmployeeRequestDto dto = new CreateEmployeeRequestDto();
        dto.setName(name);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setRepeatPassword(repeatPassword);
        dto.setDepartment(department);
        return dto;
    }

    private UpdateEmployeeRequestDto createUpdateEmployeeRequestDto(String name, String email, String department) {
        UpdateEmployeeRequestDto dto = new UpdateEmployeeRequestDto();
        dto.setName(name);
        dto.setEmail(email);
        dto.setDepartment(department);
        return dto;
    }
}
