package com.epam.rd.autocode.assessment.appliancestore.repository.appliance;

import com.epam.rd.autocode.assessment.appliancestore.model.Appliance;
import com.epam.rd.autocode.assessment.appliancestore.repository.SpecificationProvider;
import com.epam.rd.autocode.assessment.appliancestore.repository.SpecificationProviderManager;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplianceSpecificationProviderManager
        implements SpecificationProviderManager<Appliance> {
    private final List<SpecificationProvider<Appliance>> applianceSpecificationProviders;

    @Override
    public SpecificationProvider<Appliance> getSpecificationProviderByAttribute(String attribute) {
        return applianceSpecificationProviders.stream()
                .filter(p -> p.getAttribute().equals(attribute))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No relevant specification provider has been found for: " + attribute));
    }
}
