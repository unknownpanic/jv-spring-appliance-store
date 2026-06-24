package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.exception.GlobalExceptionHandler;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.CreateOrderRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.CreateOrderRowRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.OrderResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderController orderController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private final Principal mockPrincipal =
            new UsernamePasswordAuthenticationToken("user@test.com", null);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllOrders_ShouldReturnPageOfOrders() throws Exception {
        OrderResponseDto orderDto = createOrderResponseDto(1L, 100L);
        when(orderService.getAll(any())).thenReturn(new PageImpl<>(List.of(orderDto), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/orders")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].clientId").value(100L));

        verify(orderService).getAll(any());
    }

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception {
        Long id = 1L;
        OrderResponseDto responseDto = createOrderResponseDto(id, 100L);

        when(orderService.getById(id)).thenReturn(responseDto);

        mockMvc.perform(get("/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.clientId").value(100L));

        verify(orderService).getById(id);
    }

    @Test
    void createOrder_ShouldReturnCreated() throws Exception {
        CreateOrderRequestDto requestDto = createCreateOrderRequestDto(10L, 2L);
        OrderResponseDto responseDto = createOrderResponseDto(1L, 100L);

        when(orderService.create(any(CreateOrderRequestDto.class), eq("user@test.com"))).thenReturn(responseDto);

        mockMvc.perform(post("/orders")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(orderService).create(any(CreateOrderRequestDto.class), eq("user@test.com"));
    }

    @Test
    void createOrder_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CreateOrderRequestDto invalidRequest = new CreateOrderRequestDto();
        invalidRequest.setRows(Set.of());

        mockMvc.perform(post("/orders")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateOrder_ShouldReturnUpdated() throws Exception {
        Long id = 1L;
        CreateOrderRequestDto requestDto = createCreateOrderRequestDto(10L, 2L);
        OrderResponseDto responseDto = createOrderResponseDto(id, 100L);

        when(orderService.updateById(eq(id), any(CreateOrderRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(orderService).updateById(eq(id), any(CreateOrderRequestDto.class));
    }

    @Test
    void approveOrder_ShouldReturnApprovedOrder() throws Exception {
        Long id = 1L;
        OrderResponseDto responseDto = createOrderResponseDto(id, 100L);
        responseDto.setApproved(true);

        when(orderService.approveById(eq(id), eq("user@test.com"))).thenReturn(responseDto);

        mockMvc.perform(patch("/orders/{id}/approve", id)
                        .principal(mockPrincipal)) // Інжектимо Principal
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(true));

        verify(orderService).approveById(eq(id), eq("user@test.com"));
    }

    @Test
    void deleteOrder_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(orderService).deleteById(id);

        mockMvc.perform(delete("/orders/{id}", id))
                .andExpect(status().isNoContent());

        verify(orderService).deleteById(id);
    }

    private OrderResponseDto createOrderResponseDto(Long id, Long clientId) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(id);
        dto.setClientId(clientId);
        dto.setApproved(false);
        return dto;
    }

    private CreateOrderRequestDto createCreateOrderRequestDto(Long applianceId, Long quantity) {
        CreateOrderRowRequestDto row = new CreateOrderRowRequestDto();
        row.setApplianceId(applianceId);
        row.setQuantity(quantity);

        CreateOrderRequestDto dto = new CreateOrderRequestDto();
        dto.setRows(Set.of(row));
        return dto;
    }
}
