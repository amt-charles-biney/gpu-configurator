package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {

}
