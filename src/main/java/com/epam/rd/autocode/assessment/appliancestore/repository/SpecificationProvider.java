package com.epam.rd.autocode.assessment.appliancestore.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.Set;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getAttribute();

    default Specification<T> getSpecification(Set<String> params) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<String> inPredicate = criteriaBuilder.in(root.get(getAttribute()));
            params.forEach(inPredicate::value);
            return criteriaBuilder.and(inPredicate);
        };
    }
}
