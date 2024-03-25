package com.amalitech.gpuconfigurator.repository.attribute;

import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
    Optional<Attribute> findByAttributeName(String name);
    Boolean existsByAttributeName(String name);

    List<Attribute> findAllByIsRequiredTrue();
}
