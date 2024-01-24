package com.amalitech.gpuconfigurator.service.category.compatible;

import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionDTO;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.model.CompatibleOption;

import com.amalitech.gpuconfigurator.repository.CompatibleOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompatibleOptionServiceImpl implements CompatibleOptionService {

    private final CompatibleOptionRepository compatibleOptionRepository;

    @Override
    @Transactional
    public void addBulkCompatibleOptions(List<CompatibleOption> compatibleOptions) {
        compatibleOptionRepository.saveAll(compatibleOptions);
    }

    @Override
    @Transactional
    public void updateBulkCompatibleOptions(List<CompatibleOptionResponseDto> compatibleOptionResponseDtos) {

        for(CompatibleOptionResponseDto updateDto: compatibleOptionResponseDtos) {
            CompatibleOption existingOption = compatibleOptionRepository.findById(UUID.fromString(updateDto.id())).orElseThrow(() -> new EntityNotFoundException("compatible option does not exits"));

            existingOption.setName(updateDto.name());
            existingOption.setType(updateDto.type());
            existingOption.setPrice(updateDto.price());
            existingOption.setMedia(updateDto.media());
            existingOption.setUnit(updateDto.unit());
            existingOption.setIsCompatible(updateDto.isCompatible());
            existingOption.setIsIncluded(updateDto.isIncluded());
            existingOption.setIsMeasured(updateDto.isMeasured());
            existingOption.setPriceFactor(updateDto.priceFactor());
            existingOption.setPriceIncrement(updateDto.priceIncrement());
            existingOption.setBaseAmount(updateDto.baseAmount());
            existingOption.setMaxAmount(updateDto.maxAmount());

            compatibleOptionRepository.save(existingOption);
        }
    }

    @Override
    public List<CompatibleOption> getAllCompatibleOptionsByCategoryConfig(UUID configId) {
        return compatibleOptionRepository.findAllByCategoryConfigId(configId);
    }

    @Override
    public GenericResponse addCompatibleOption(CompatibleOptionDTO option) {
        CompatibleOption compatibleOption = CompatibleOption.builder()
                .name(option.name())
                .price(option.price())
                .type(option.type())
                .isIncluded(option.isIncluded())
                .isCompatible(option.isCompatible())
                .categoryConfig(option.categoryConfig())
                .baseAmount(option.baseAmount())
                .priceFactor(option.priceFactor())
                .priceIncrement(option.priceIncrement())
                .isMeasured(option.isMeasured())
                .media(option.media())
                .build();
        compatibleOptionRepository.save(compatibleOption);
        return new GenericResponse(201, "compatible option created");
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
