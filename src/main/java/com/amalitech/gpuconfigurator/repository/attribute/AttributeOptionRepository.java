package com.amalitech.gpuconfigurator.repository.attribute;

import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttributeOptionRepository extends JpaRepository<AttributeOption, UUID> {
}