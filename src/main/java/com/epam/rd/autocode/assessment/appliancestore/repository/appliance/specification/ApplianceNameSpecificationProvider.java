package com.epam.rd.autocode.assessment.appliancestore.repository.appliance.specification;

import com.epam.rd.autocode.assessment.appliancestore.model.Appliance;
import com.epam.rd.autocode.assessment.appliancestore.repository.SpecificationProvider;
import org.springframework.stereotype.Component;

@Component
public class ApplianceNameSpecificationProvider implements SpecificationProvider<Appliance> {
    private static final String ATTRIBUTE = "name";

    @Override
    public String getAttribute() {
        return ATTRIBUTE;
    }
}
