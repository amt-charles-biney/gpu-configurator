package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.dto.shipping.ShippingResponse;
import com.amalitech.gpuconfigurator.model.Shipping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, UUID> {
    ShippingResponse findShippingResponseById(UUID shippingId);

    Page<ShippingResponse> findAllBy(Pageable pageable);

    Optional<ShippingResponse> findOptionalShippingResponseById(UUID shippingId);
}
