package com.epam.rd.autocode.assessment.appliancestore.controller;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.CreateOrderRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.OrderResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
        name = "Order management",
        description = "Endpoints for managing client orders"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "Get all orders",
            description = "Returns a paginated list of all orders. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Page<OrderResponseDto> getAllOrders(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return orderService.getAll(pageable);
    }

    @Operation(
            summary = "Get my orders",
            description = "Returns a paginated list of orders for the authenticated client. "
                    + " CLIENT role required."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    public Page<OrderResponseDto> getMyOrders(
            @Parameter(hidden = true) Authentication authentication,
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        String userEmail = authentication.getName();
        return orderService.getMyOrders(userEmail, pageable);
    }

    @Operation(
            summary = "Get order by ID",
            description = "Returns order details by identifier. CLIENT or EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE')")
    public OrderResponseDto getOrderById(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id
    ) {
        return orderService.getById(id);
    }

    @Operation(
            summary = "Create order",
            description = "Creates a new order. CLIENT or EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE')")
    public OrderResponseDto createOrder(
            @RequestBody @Valid CreateOrderRequestDto requestDto,
            @Parameter(hidden = true) Authentication authentication
    ) {
        String userEmail = authentication.getName();
        return orderService.create(requestDto, userEmail);
    }

    @Operation(
            summary = "Update order",
            description = "Updates an existing order by ID. CLIENT or EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE')")
    public OrderResponseDto updateOrder(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid CreateOrderRequestDto requestDto
    ) {
        return orderService.updateById(id, requestDto);
    }

    @Operation(
            summary = "Approve order",
            description = "Approves a pending order. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order approved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public OrderResponseDto approveOrder(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) Authentication authentication
    ) {
        String employeeEmail = authentication.getName();
        return orderService.approveById(id, employeeEmail);
    }

    @Operation(
            summary = "Delete order",
            description = "Deletes an order by ID. EMPLOYEE role required."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id
    ) {
        orderService.deleteById(id);
    }
}
