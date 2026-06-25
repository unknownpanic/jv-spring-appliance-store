package com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;

@Data
@Schema(description = "Parameters for searching and filtering appliances")
public class ApplianceSearchParametersDto {

    @Schema(
            description = "List of appliance names to search for",
            example = "[\"Washing Machine\", \"Refrigerator\"]"
    )
    private Set<String> names = new LinkedHashSet<>();
}
