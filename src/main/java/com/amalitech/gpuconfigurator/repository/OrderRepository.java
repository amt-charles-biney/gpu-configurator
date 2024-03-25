package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Order;
import com.amalitech.gpuconfigurator.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    Page<Order> findAllByUser(User user, Pageable pageable);

    Optional<Order> findByTrackingId(String trackingCode);

    @Query("SELECT u, COUNT(o) " +
            "FROM User u " +
            "LEFT JOIN u.orders o " +
            "GROUP BY u.id " +
            "HAVING COUNT(o) > 0")
    Page<Object[]> selectAllUsersWithOrderCount(Pageable pageable);

    @Query("SELECT COUNT(DISTINCT u) FROM User u LEFT JOIN u.orders o")
    Long customers();

    @Query("SELECT COUNT(u) FROM Order u")
    Long orders();
}
