package com.epam.rd.autocode.assessment.appliancestore.repository;

import com.epam.rd.autocode.assessment.appliancestore.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"client", "employee", "orderRowSet"})
    List<Order> findAll();

    @Query("SELECT o FROM Order o "
            + "JOIN FETCH o.client "
            + "LEFT JOIN FETCH o.employee "
            + "LEFT JOIN FETCH o.orderRowSet "
            + "WHERE o.id = :id")
    Optional<Order> findById(@Param("id") Long id);

    Order save(Order order);

    void deleteById(Long id);
}
