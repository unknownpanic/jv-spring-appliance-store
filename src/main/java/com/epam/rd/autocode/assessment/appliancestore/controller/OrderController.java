package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.CreateOrderRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.OrderResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Page<OrderResponseDto> getAllOrders(
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return orderService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public OrderResponseDto getOrderById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE')")
    public OrderResponseDto createOrder(
            @RequestBody @Valid CreateOrderRequestDto requestDto,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        return orderService.create(requestDto, userEmail);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE')")
    public OrderResponseDto updateOrder(
            @PathVariable Long id,
            @RequestBody @Valid CreateOrderRequestDto requestDto
    ) {
        return orderService.updateById(id, requestDto);
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public OrderResponseDto approveOrder(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String employeeEmail = authentication.getName();
        return orderService.approveById(id, employeeEmail);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
    }
}
