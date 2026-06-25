package com.epam.rd.autocode.assessment.appliancestore.repository;

import jakarta.persistence.criteria.Predicate;
import java.util.Set;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getAttribute();

    default Specification<T> getSpecification(Set<String> params) {
        return (root, query, criteriaBuilder) -> {
            if (params == null || params.isEmpty()) {
                return criteriaBuilder.disjunction();
            }
            Predicate[] likes = params.stream()
                    .map(param -> criteriaBuilder.like(
                            root.get(getAttribute()),
                            "%" + param + "%"
                    ))
                    .toArray(Predicate[]::new);

            return criteriaBuilder.or(likes);
        };
    }
}
