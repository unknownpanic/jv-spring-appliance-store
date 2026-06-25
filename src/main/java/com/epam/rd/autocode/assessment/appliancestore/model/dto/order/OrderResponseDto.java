package com.epam.rd.autocode.assessment.appliancestore.model.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;

@Data
@Schema(description = "Order information")
public class OrderResponseDto {

    @Schema(
            description = "Order identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Client identifier",
            example = "5"
    )
    private Long clientId;

    @Schema(
            description = "Employee identifier who processed the order",
            example = "2"
    )
    private Long employeeId;

    @Schema(
            description = "Order approval status",
            example = "false"
    )
    private boolean approved;

    @Schema(description = "Order items")
    private Set<OrderRowResponseDto> rows;
}
