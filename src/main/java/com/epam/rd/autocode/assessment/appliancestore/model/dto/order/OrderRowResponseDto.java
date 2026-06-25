package com.epam.rd.autocode.assessment.appliancestore.model.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Schema(description = "Order item information")
public class OrderRowResponseDto {

    @Schema(
            description = "Order row identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Appliance identifier",
            example = "3"
    )
    private Long applianceId;

    @Schema(
            description = "Quantity of appliances",
            example = "2"
    )
    private Long number;

    @Schema(
            description = "Total amount for this order row",
            example = "1599.98"
    )
    private BigDecimal amount;
}
