package com.epam.rd.autocode.assessment.appliancestore.repository;

import com.epam.rd.autocode.assessment.appliancestore.model.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;

public interface EmployeeRepository extends BaseUserRepository<Employee> {
    @EntityGraph(attributePaths = "roles")
    List<Employee> findAll();
}
