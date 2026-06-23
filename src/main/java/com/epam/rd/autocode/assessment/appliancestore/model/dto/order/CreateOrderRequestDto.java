package com.epam.rd.autocode.assessment.appliancestore.model.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotEmpty
    private Set<@Valid CreateOrderRowRequestDto> rows;
}
