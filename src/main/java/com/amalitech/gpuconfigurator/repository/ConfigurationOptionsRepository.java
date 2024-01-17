package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConfigurationOptionsRepository extends JpaRepository<ConfigOptions, UUID> {
}
