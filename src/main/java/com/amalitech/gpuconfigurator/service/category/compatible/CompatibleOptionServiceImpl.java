package com.amalitech.gpuconfigurator.service.category.compatible;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleUpdateDto;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;

import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.CompatibleOptionRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompatibleOptionServiceImpl implements CompatibleOptionService {

    private final CompatibleOptionRepository compatibleOptionRepository;
    private final AttributeOptionRepository attributeOptionRepository;

    @Override
    @Transactional
    public void addBulkCompatibleOptions(List<CompatibleOption> compatibleOptions) {
        compatibleOptionRepository.saveAll(compatibleOptions);
    }

    @Override
    public void updateBulkCompatibleOptions(List<CompatibleUpdateDto> compatibleUpdateDtos) {

    }

    @Override
    @Transactional
    public void updateBulkCompatibleOptions(CategoryConfig categoryConfig, List<CompatibleUpdateDto> compatibleUpdateDtos) {

        for (CompatibleUpdateDto updateDto : compatibleUpdateDtos) {
            // find the compatible option that has the categoryConfig id as well as the attribute option id
            Optional<CompatibleOption> optionalExistingOption =
                    compatibleOptionRepository.findByCategoryConfigIdAndAttributeOptionId(categoryConfig.getId(), UUID.fromString(updateDto.attributeOptionId()));

            CompatibleOption existingOption = optionalExistingOption.orElse(new CompatibleOption());
            AttributeOption attributeOption = attributeOptionRepository.findById(UUID.fromString(updateDto.attributeOptionId()))
                            .orElseThrow(() -> new EntityNotFoundException("attribute option could not be found"));

            existingOption.setIsCompatible(updateDto.isCompatible());
            existingOption.setIsIncluded(updateDto.isIncluded());
            existingOption.setIsMeasured(updateDto.isMeasured());
            existingOption.setAttributeOption(attributeOption);
            existingOption.setCategoryConfig(categoryConfig);
            existingOption.setIsMeasured(updateDto.isMeasured());
            existingOption.setSize(updateDto.size());
            existingOption.setUpdatedAt(LocalDateTime.now());

            compatibleOptionRepository.save(existingOption);
        }
    }

    @Override
    public List<CompatibleOption> getAllCompatibleOptionsByCategoryConfig(UUID configId) {
        return compatibleOptionRepository.findAllByCategoryConfigId(configId);
    }

    @Override
    public GenericResponse deleteAllCompatibleOptions() {
        compatibleOptionRepository.deleteAll();
        return new GenericResponse(201, "deleted all compatible options");
    }

    @Override
    public GenericResponse deleteCompatibleOption(String id) {
        compatibleOptionRepository.deleteById(UUID.fromString(id));
        return new GenericResponse(201, "deleted compatible option");
    }

    @Override
    public List<CompatibleOption> getByCategoryConfigId(UUID id) {
        return compatibleOptionRepository.getByCategoryConfigId(id);
    }
}
