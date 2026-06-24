package com.epam.rd.autocode.assessment.appliancestore.repository.appliance;

import com.epam.rd.autocode.assessment.appliancestore.model.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplianceRepository
        extends JpaRepository<Appliance, Long>, JpaSpecificationExecutor<Appliance> {
}
