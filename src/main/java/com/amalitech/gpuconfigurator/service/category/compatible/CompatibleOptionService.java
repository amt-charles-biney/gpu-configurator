package com.amalitech.gpuconfigurator.service.category.compatible;

import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionDTO;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.model.category.CompatibleOption;
import com.amalitech.gpuconfigurator.repository.category.CompatibleOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompatibleOptionService {

    private final CompatibleOptionRepository compatibleOptionRepository;

    @Transactional
    public void addBulkCompatibleOptions(List<CompatibleOption> compatibleOptions) {
        compatibleOptionRepository.saveAll(compatibleOptions);
    }

    public GenericResponse addCompatibleOption(CompatibleOptionDTO option) {
        CompatibleOption compatibleOption = CompatibleOption.builder().isCompatible(option.isCompatible()).price(option.price()).name(option.name()).type(option.type()).isIncluded(option.isIncluded()).isCompatible(option.isCompatible()).categoryConfig(option.categoryConfig()).build();
        compatibleOptionRepository.save(compatibleOption);
        return new GenericResponse(201, "compatible option created");
    }

    public GenericResponse updateCompatible(String id, CompatibleOptionDTO compatibleOptionDTO) {
        CompatibleOption compatible = compatibleOptionRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("could not update option"));

        return new GenericResponse(201, "compatibility is " + compatible);
    }

    public GenericResponse deleteAllCompatibleOptions() {
        compatibleOptionRepository.deleteAll();
        return new GenericResponse(201, "deleted all compatible options");
    }

    public GenericResponse deleteCompatibleOption(String id) {
        compatibleOptionRepository.deleteById(UUID.fromString(id));
        return new GenericResponse(201, "deleted compatible option");
    }

    public List<CompatibleOption> getByCategoryConfigId(UUID id) {
        return compatibleOptionRepository.getByCategoryConfigId(id);
    }
}
