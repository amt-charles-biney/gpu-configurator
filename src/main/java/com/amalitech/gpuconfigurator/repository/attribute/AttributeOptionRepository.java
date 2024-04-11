package com.amalitech.gpuconfigurator.repository.attribute;

import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttributeOptionRepository extends JpaRepository<AttributeOption, UUID> {
    Optional<AttributeOption> findByOptionName(String name);
    Optional<List<AttributeOption>> findAllByAttributeId(UUID id);

    void deleteByOptionName(String name);

    List<AttributeOption> findByInStockLessThan(int i);
}
