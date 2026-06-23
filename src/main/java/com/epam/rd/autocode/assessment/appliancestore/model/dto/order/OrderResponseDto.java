package com.epam.rd.autocode.assessment.appliancestore.model.dto.order;

import java.util.Set;
import lombok.Data;

@Data
public class OrderResponseDto {
    private Long id;
    private Long clientId;
    private Long employeeId;
    private boolean approved;
    private Set<OrderRowResponseDto> rows;
}
