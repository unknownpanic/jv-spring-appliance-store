package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.CreateOrderRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.OrderResponseDto;
import java.util.List;

public interface OrderService {
    List<OrderResponseDto> getAll();

    OrderResponseDto create(CreateOrderRequestDto requestDto, String email);

    OrderResponseDto getById(Long id);

    OrderResponseDto approveById(Long id, String employeeEmail);

    OrderResponseDto updateById(Long id, CreateOrderRequestDto requestDto);

    void deleteById(Long id);
}
