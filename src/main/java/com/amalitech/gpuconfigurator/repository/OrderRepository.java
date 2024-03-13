package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Order;
import com.amalitech.gpuconfigurator.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    Page<Order> findAllByUser(User user, Pageable pageable);
}
