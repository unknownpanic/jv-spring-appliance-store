package com.epam.rd.autocode.assessment.appliancestore.model.dto.order;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderRowResponseDto {
    private Long id;
    private Long applianceId;
    private String applianceName;
    private Long number;
    private BigDecimal amount;
}
