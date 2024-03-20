package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishListRepository extends JpaRepository<WishList, UUID> {
    Optional<WishList> findByUserId(UUID userId);

    Optional<WishList> findByUserSessionId(UUID userSessionId);
}
