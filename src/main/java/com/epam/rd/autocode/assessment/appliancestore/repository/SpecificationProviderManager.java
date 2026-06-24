package com.epam.rd.autocode.assessment.appliancestore.repository;

import org.springframework.stereotype.Component;

@Component
public interface SpecificationProviderManager<T> {
    SpecificationProvider<T> getSpecificationProviderByAttribute(String attribute);
}
