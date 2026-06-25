package com.epam.rd.autocode.assessment.appliancestore.service.impl;

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
import com.epam.rd.autocode.assessment.appliancestore.service.OrderService;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ApplianceRepository applianceRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getById(Long id) {
        return orderMapper.toDto(orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Client not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getMyOrders(String userEmail, Pageable pageable) {
        return orderRepository.findAllByClientEmail(userEmail, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderResponseDto create(CreateOrderRequestDto requestDto, String userEmail) {
        Client client = clientRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Order order = new Order();
        order.setClient(client);
        order.setApproved(false);
        order.setOrderRowSet(buildOrderRows(requestDto));

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponseDto updateById(Long id, CreateOrderRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.getOrderRowSet().clear();
        order.getOrderRowSet().addAll(buildOrderRows(requestDto));
        order.setApproved(false);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponseDto approveById(Long id, String employeeEmail) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        Employee employee = employeeRepository.findByEmail(employeeEmail)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        order.setEmployee(employee);
        order.setApproved(true);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found");
        }
        orderRepository.deleteById(id);
    }

    private Set<OrderRow> buildOrderRows(CreateOrderRequestDto requestDto) {
        Set<OrderRow> orderRows = new HashSet<>();
        for (CreateOrderRowRequestDto rowDto : requestDto.getRows()) {
            Appliance appliance = applianceRepository.findById(rowDto.getApplianceId())
                    .orElseThrow(() -> new EntityNotFoundException("Appliance not found"));

            OrderRow orderRow = new OrderRow();
            orderRow.setAppliance(appliance);
            orderRow.setNumber(rowDto.getQuantity());
            orderRow.setAmount(appliance.getPrice()
                    .multiply(BigDecimal.valueOf(rowDto.getQuantity())));
            orderRows.add(orderRow);
        }
        return orderRows;
    }
}
