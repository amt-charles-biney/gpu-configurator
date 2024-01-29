package com.amalitech.gpuconfigurator.service.category.compatible;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionDTO;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleUpdateDto;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface CompatibleOptionService {
    @Transactional
    void addBulkCompatibleOptions(List<CompatibleOption> compatibleOptions);
    @Transactional
    void updateBulkCompatibleOptions(List<CompatibleUpdateDto> compatibleUpdateDtos);

    List<CompatibleOption> getAllCompatibleOptionsByCategoryConfig(UUID configId);

    GenericResponse deleteAllCompatibleOptions();

    GenericResponse deleteCompatibleOption(String id);

    List<CompatibleOption> getByCategoryConfigId(UUID id);
}
