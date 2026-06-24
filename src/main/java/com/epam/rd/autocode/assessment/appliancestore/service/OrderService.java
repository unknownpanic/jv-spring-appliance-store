package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.CreateOrderRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderResponseDto> getAll(Pageable pageable);

    OrderResponseDto create(CreateOrderRequestDto requestDto, String email);

    OrderResponseDto getById(Long id);

    OrderResponseDto approveById(Long id, String employeeEmail);

    OrderResponseDto updateById(Long id, CreateOrderRequestDto requestDto);

    void deleteById(Long id);
}
