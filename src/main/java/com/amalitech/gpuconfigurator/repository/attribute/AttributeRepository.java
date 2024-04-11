package com.amalitech.gpuconfigurator.repository.attribute;

import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
    Optional<Attribute> findByAttributeName(String name);
    Boolean existsByAttributeName(String name);

    List<Attribute> findAllByIsRequiredTrue();

    Page<Attribute> findAllByAttributeNameContainingIgnoreCase(String query, PageRequest pageRequest);
}
