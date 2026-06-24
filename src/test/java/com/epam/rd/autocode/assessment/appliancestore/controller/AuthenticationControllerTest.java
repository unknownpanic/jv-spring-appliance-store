package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.exception.GlobalExceptionHandler;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.ClientResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.CreateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.user.UserLoginRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.user.UserLoginResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.security.AuthenticationService;
import com.epam.rd.autocode.assessment.appliancestore.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private ClientService clientService;
    @InjectMocks
    private AuthenticationController authenticationController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authenticationController)
                 .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void login_ShouldReturnToken() throws Exception {
        UserLoginRequestDto requestDto = createUserLoginRequestDto("test@example.com", "Str0ngP@ssword!");
        UserLoginResponseDto responseDto = new UserLoginResponseDto("mocked-jwt-token");

        when(authenticationService.authenticate(any(UserLoginRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));

        verify(authenticationService).authenticate(any(UserLoginRequestDto.class));
    }

    @Test
    void login_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        UserLoginRequestDto invalidRequest = new UserLoginRequestDto();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_ShouldReturnCreatedClient() throws Exception {
        CreateClientRequestDto requestDto = createCreateClientRequestDto(
                "John Doe",
                "john.doe@example.com",
                "Str0ngP@ssword123!",
                "Str0ngP@ssword123!",
                "1234-5678-1234-5678"
        );

        ClientResponseDto responseDto = createClientResponseDto(
                1L,
                "John Doe",
                "john.doe@example.com"
        );

        when(clientService.create(any(CreateClientRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(clientService).create(any(CreateClientRequestDto.class));
    }

    @Test
    void register_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CreateClientRequestDto invalidRequest = new CreateClientRequestDto();

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    private UserLoginRequestDto createUserLoginRequestDto(String email, String password) {
        UserLoginRequestDto dto = new UserLoginRequestDto();
        dto.setEmail(email);
        dto.setPassword(password);
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

    private ClientResponseDto createClientResponseDto(Long id, String name, String email) {
        ClientResponseDto dto = new ClientResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        return dto;
    }
}
