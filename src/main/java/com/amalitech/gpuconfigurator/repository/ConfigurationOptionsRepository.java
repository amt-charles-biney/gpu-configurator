package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfigurationOptionsRepository extends JpaRepository<ConfigOptions, UUID> {


    @Query("SELECT s FROM Configuration_Options s WHERE s.optionType = ?1 ")
    Optional<ConfigOptions> findByType(String type);
}
