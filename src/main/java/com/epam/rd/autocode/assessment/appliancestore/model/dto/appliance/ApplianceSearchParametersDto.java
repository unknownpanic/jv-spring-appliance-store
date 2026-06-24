package com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance;

import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;

@Data
public class ApplianceSearchParametersDto {
    private Set<String> names = new LinkedHashSet<>();
    private Set<String> categories = new LinkedHashSet<>();
}
