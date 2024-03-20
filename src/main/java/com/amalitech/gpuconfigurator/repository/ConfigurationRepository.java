package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, UUID> {
    long countByCartId(UUID cardId);

    Optional<Configuration> findByIdAndCartId(UUID configuredProductId, UUID cardId);

    List<Configuration> findByCartId(UUID cartId);

    void deleteByCartId(UUID cartId);

    Page<Configuration> findByWishListId(UUID wishListId, Pageable pageable);

    Optional<Configuration> findByIdAndWishListId(UUID configuredProductId, UUID wishListId);
}
