package com.amalitech.gpuconfigurator.service.category.compatible;

import com.amalitech.gpuconfigurator.model.CompatibleOption;

import java.util.List;
import java.util.UUID;

public interface CompatibleOptionService {
    List<CompatibleOption> getByCategoryConfigId(UUID id);
}