package com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ApplianceResponseDto {
    private Long id;
    private String name;
    private String category;
    private String model;
    private String manufacturerId;
    private String powerType;
    private String characteristic;
    private String description;
    private Integer power;
    private BigDecimal price;
}
