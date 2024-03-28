package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.CompatibleOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompatibleOptionRepository extends JpaRepository<CompatibleOption, UUID> {
    List<CompatibleOption> getByCategoryConfigId(UUID id);

    List<CompatibleOption> findAllByCategoryConfigId(UUID configId);

    Optional<CompatibleOption> findByCategoryConfigIdAndAttributeOptionId(UUID categoryConfigId, UUID attributeOptionId);

    List<CompatibleOption> findAllByAttributeOptionId(UUID uuid);

    @Query("SELECT co FROM CompatibleOption co WHERE co.id IN :uuids")
    List<CompatibleOption> findAllIdsIn(@Param("uuids") List<UUID> uuids);
}
