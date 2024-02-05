package com.amalitech.gpuconfigurator.service.category.compatible;

import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.repository.CompatibleOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompatibleOptionServiceImpl implements CompatibleOptionService {
    private final CompatibleOptionRepository compatibleOptionRepository;

    @Override
    public List<CompatibleOption> getByCategoryConfigId(UUID id) {
        return compatibleOptionRepository.getByCategoryConfigId(id);
    }
}