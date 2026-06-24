package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.exception.GlobalExceptionHandler;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.ClientResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.CreateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.UpdateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
class ClientControllerTest {
    @Mock
    private ClientService clientService;
    @InjectMocks
    private ClientController clientController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(clientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAll_ShouldReturnListOfClients() throws Exception {
        List<ClientResponseDto> clients = Arrays.asList(
                createClientResponseDto(1L, "Alice Smith", "alice@example.com"),
                createClientResponseDto(2L, "Bob Jones", "bob@example.com")
        );

        when(clientService.getAll()).thenReturn(clients);

        mockMvc.perform(get("/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Alice Smith"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Bob Jones"));

        verify(clientService).getAll();
    }

    @Test
    void getById_ShouldReturnClient() throws Exception {
        Long id = 1L;
        ClientResponseDto client = createClientResponseDto(id, "Alice Smith", "alice@example.com");

        when(clientService.getById(id)).thenReturn(client);

        mockMvc.perform(get("/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Alice Smith"));

        verify(clientService).getById(id);
    }

    @Test
    void create_ShouldReturnCreatedClient() throws Exception {
        CreateClientRequestDto requestDto = createCreateClientRequestDto(
                "Charlie Brown", "charlie@example.com", "Password123!", "Password123!", "1234-1234-1234-1234"
        );
        ClientResponseDto responseDto = createClientResponseDto(3L, "Charlie Brown", "charlie@example.com");

        when(clientService.create(any(CreateClientRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Charlie Brown"));

        verify(clientService).create(any(CreateClientRequestDto.class));
    }

    @Test
    void create_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CreateClientRequestDto invalidRequest = new CreateClientRequestDto();

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_ShouldReturnUpdatedClient() throws Exception {
        Long id = 1L;
        UpdateClientRequestDto requestDto = createUpdateClientRequestDto("Alice Updated", "alice.new@example.com", "8765-4321-8765-4321");
        ClientResponseDto responseDto = createClientResponseDto(id, "Alice Updated", "alice.new@example.com");

        when(clientService.updateById(eq(id), any(UpdateClientRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Alice Updated"));

        verify(clientService).updateById(eq(id), any(UpdateClientRequestDto.class));
    }

    @Test
    void update_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Long id = 1L;
        UpdateClientRequestDto invalidRequest = new UpdateClientRequestDto();

        mockMvc.perform(put("/clients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(clientService).deleteById(id);

        mockMvc.perform(delete("/clients/{id}", id))
                .andExpect(status().isNoContent());

        verify(clientService).deleteById(id);
    }

    private ClientResponseDto createClientResponseDto(Long id, String name, String email) {
        ClientResponseDto dto = new ClientResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        return dto;
    }

    private CreateClientRequestDto createCreateClientRequestDto(
            String name, String email, String password, String repeatPassword, String card) {
        CreateClientRequestDto dto = new CreateClientRequestDto();
        dto.setName(name);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setRepeatPassword(repeatPassword);
        dto.setCard(card);
        return dto;
    }

    private UpdateClientRequestDto createUpdateClientRequestDto(String name, String email, String card) {
        UpdateClientRequestDto dto = new UpdateClientRequestDto();
        dto.setName(name);
        dto.setEmail(email);
        dto.setCard(card);
        return dto;
    }
}
