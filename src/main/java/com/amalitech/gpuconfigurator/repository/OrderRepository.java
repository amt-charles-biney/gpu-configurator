package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Order;
import com.amalitech.gpuconfigurator.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    Page<Order> findAllByUser(User user, Pageable pageable);

    Optional<Order> findByTrackingId(String trackingCode);


    @Query("SELECT DISTINCT u, COUNT(o), SUM(p.amount), o.createdAt " +
            "FROM User u " +
            "LEFT JOIN u.orders o " +
            "LEFT JOIN o.payment p " +
            "WHERE o IS NOT NULL " +
            "GROUP BY u.id, o.createdAt " +
            "HAVING COUNT(o) > 0")
    Page<Object[]> selectAllUsersWithOrderCount(Pageable pageable);

    @Query("SELECT COUNT(DISTINCT u) FROM User u LEFT JOIN u.orders o WHERE o IS NOT NULL")
    Long customers();

    @Query("SELECT COUNT(u) FROM Order u")
    Long orders();

    @Query("SELECT COUNT(u) FROM Order u WHERE u.status = 'Delivered' AND MONTH(u.createdAt) = :monthValue AND YEAR(u.createdAt) = :yearValue")
    Long deliveredStatusCount(@Param("monthValue") int monthValue, @Param("yearValue") int yearValue);
}
