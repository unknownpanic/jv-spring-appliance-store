package com.epam.rd.autocode.assessment.appliancestore.repository;

import com.epam.rd.autocode.assessment.appliancestore.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"client", "employee", "orderRowSet"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"client", "employee", "orderRowSet"})
    Optional<Order> findById(Long id);

    Order save(Order order);

    void deleteById(Long id);
}
