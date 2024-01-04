package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.CompatibleOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompatibleOptionRepository extends JpaRepository<CompatibleOption, UUID> {
    List<CompatibleOption> getByCategoryConfigId(UUID id);
}
