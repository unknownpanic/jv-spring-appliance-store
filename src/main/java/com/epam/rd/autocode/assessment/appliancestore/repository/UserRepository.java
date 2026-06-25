package com.epam.rd.autocode.assessment.appliancestore.repository;

import com.epam.rd.autocode.assessment.appliancestore.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;

public interface UserRepository extends BaseUserRepository<User> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);
}
