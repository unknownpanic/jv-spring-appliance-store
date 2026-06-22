package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.CreateOrderRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.OrderResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public Page<OrderResponseDto> getAll(Pageable pageable) {
        return orderService.getAll(pageable);
    }

    public OrderResponseDto getById(Long id) {
        return orderService.getById(id);
    }

    public OrderResponseDto create(CreateOrderRequestDto requestDto) {
        return orderService.create(requestDto);
    }

    public OrderResponseDto updateById(Long id, CreateOrderRequestDto requestDto) {
        return orderService.updateById(id, requestDto);
    }

    public void deleteById(Long id) {
        return;
    }
}
