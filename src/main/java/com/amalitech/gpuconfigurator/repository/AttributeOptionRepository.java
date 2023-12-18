package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.AttributeOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttributeOptionRepository extends JpaRepository<AttributeOption, UUID> {
    Optional<AttributeOption> findByOptionName(String name);
    Optional<List<AttributeOption>> findAllByAttributeId(UUID id);

    void deleteByName(String name);

}
