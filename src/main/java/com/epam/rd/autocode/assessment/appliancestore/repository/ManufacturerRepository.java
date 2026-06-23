package com.epam.rd.autocode.assessment.appliancestore.repository;

import com.epam.rd.autocode.assessment.appliancestore.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
}
