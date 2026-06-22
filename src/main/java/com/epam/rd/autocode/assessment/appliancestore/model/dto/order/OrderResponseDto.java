package com.epam.rd.autocode.assessment.appliancestore.model.dto.order;

import lombok.Data;
import java.util.Set;

@Data
public class OrderResponseDto {
    private Long id;
    private Long clientId;
    private String clientName;
    private Long employeeId;
    private Boolean approved;
    private Set<OrderRowResponseDto> rows;
}
