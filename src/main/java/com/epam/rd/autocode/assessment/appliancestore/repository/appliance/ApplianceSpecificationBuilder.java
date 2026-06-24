package com.epam.rd.autocode.assessment.appliancestore.repository.appliance;

import com.epam.rd.autocode.assessment.appliancestore.model.Appliance;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceSearchParametersDto;
import com.epam.rd.autocode.assessment.appliancestore.repository.SpecificationBuilder;
import com.epam.rd.autocode.assessment.appliancestore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplianceSpecificationBuilder
        implements SpecificationBuilder<Appliance, ApplianceSearchParametersDto> {
    private final SpecificationProviderManager<Appliance> specificationProviderManager;

    @Override
    public Specification<Appliance> build(ApplianceSearchParametersDto searchParameters) {
        Specification<Appliance> spec = Specification.unrestricted();

        if (searchParameters.getNames() != null
                && !searchParameters.getNames().isEmpty()) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProviderByAttribute("name")
                    .getSpecification(searchParameters.getNames()));
        }

        if (searchParameters.getCategories() != null
                && !searchParameters.getCategories().isEmpty()) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProviderByAttribute("category")
                    .getSpecification(searchParameters.getCategories()));
        }

        return spec;
    }
}
