package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.exception.EntityNotFoundException;
import com.epam.rd.autocode.assessment.appliancestore.mapper.OrderMapper;
import com.epam.rd.autocode.assessment.appliancestore.model.Appliance;
import com.epam.rd.autocode.assessment.appliancestore.model.Client;
import com.epam.rd.autocode.assessment.appliancestore.model.Employee;
import com.epam.rd.autocode.assessment.appliancestore.model.Order;
import com.epam.rd.autocode.assessment.appliancestore.model.OrderRow;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.CreateOrderRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.CreateOrderRowRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.OrderResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliancestore.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliancestore.repository.OrderRepository;
import com.epam.rd.autocode.assessment.appliancestore.repository.appliance.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliancestore.service.impl.OrderServiceImpl;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ApplianceRepository applianceRepository;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderServiceImpl orderService;

    private Appliance appliance;
    private CreateOrderRequestDto requestDto;

    @BeforeEach
    void setUp() {
        appliance = new Appliance();
        appliance.setId(10L);
        appliance.setPrice(new BigDecimal("100.00"));

        CreateOrderRowRequestDto row = new CreateOrderRowRequestDto();
        row.setApplianceId(10L);
        row.setQuantity(3L);

        requestDto = new CreateOrderRequestDto();
        requestDto.setRows(Set.of(row));
    }

    @Test
    void create_ShouldSucceed_WhenClientAndApplianceExist() {
        Client client = new Client();
        when(clientRepository.findByEmail("test@email.com")).thenReturn(Optional.of(client));
        when(applianceRepository.findById(10L)).thenReturn(Optional.of(appliance));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderResponseDto());

        orderService.create(requestDto, "test@email.com");

        verify(orderRepository).save(argThat(order ->
                !order.isApproved() && order.getOrderRowSet().size() == 1
        ));
    }

    @Test
    void create_ShouldThrowException_WhenApplianceNotFound() {
        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.of(new Client()));
        when(applianceRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.create(requestDto, "test@email.com"));
    }

    @Test
    void updateById_ShouldClearAndAddRows() {
        Order existingOrder = new Order();
        existingOrder.setOrderRowSet(new HashSet<>(Set.of(new OrderRow())));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(applianceRepository.findById(10L)).thenReturn(Optional.of(appliance));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderResponseDto());

        orderService.updateById(1L, requestDto);

        assertEquals(1, existingOrder.getOrderRowSet().size());
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void approveById_ShouldSetApprovedTrue() {
        Order order = new Order();
        Employee employee = new Employee();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(employeeRepository.findByEmail("emp@test.com")).thenReturn(Optional.of(employee));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderResponseDto());

        orderService.approveById(1L, "emp@test.com");

        assertTrue(order.isApproved());
        assertEquals(employee, order.getEmployee());
        verify(orderRepository).save(order);
    }

    @Test
    void getById_ShouldReturnOrderResponseDto_WhenFound() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(responseDto);

        OrderResponseDto result = orderService.getById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotFound() {
        when(orderRepository.existsById(1L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> orderService.deleteById(1L));
    }

    @Test
    void deleteById_ShouldSuccess_WhenExists() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        orderService.deleteById(1L);
        verify(orderRepository).deleteById(1L);
    }

    @Test
    void getAll_ShouldReturnPage() {
        Page<Order> page = new PageImpl<>(List.of(new Order()));
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderResponseDto());

        Page<OrderResponseDto> result = orderService.getAll(PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
        verify(orderRepository).findAll(any(Pageable.class));
    }
}
