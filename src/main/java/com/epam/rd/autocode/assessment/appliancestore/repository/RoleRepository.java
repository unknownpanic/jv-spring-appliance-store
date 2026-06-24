package com.epam.rd.autocode.assessment.appliancestore.repository;

import com.epam.rd.autocode.assessment.appliancestore.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Role.RoleName name);
}
