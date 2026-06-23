package com.epam.rd.autocode.assessment.appliancestore.repository;

import com.epam.rd.autocode.assessment.appliancestore.model.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplianceRepository extends JpaRepository<Appliance, Long> {
}
