package com.amalitech.gpuconfigurator.service.category.compatible;

import com.amalitech.gpuconfigurator.dto.CompatibleOptionDto;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.model.category.CompatibleOption;
import com.amalitech.gpuconfigurator.repository.category.CompatibleOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompatibleOptionService {

    private final CompatibleOptionRepository compatibleOptionRepository;

    @Transactional
    public void addBulkCompatibleOptions(List<CompatibleOptionDto> compatibleOptionDto) {
         List<CompatibleOption> compatibleOptions = compatibleOptionDto.stream().map(option -> CompatibleOption.builder()
                .isCompatible(option.isCompatible())
                .attributeOption(option.attributeOption())
                .categoryConfig(option.categoryConfig())
                .attribute(option.attribute())
                .build()).toList();

         compatibleOptionRepository.saveAll(compatibleOptions);
    }

    public GenericResponse addCompatibleOption(CompatibleOptionDto compatibleOptionDto) {
        CompatibleOption compatibleOption = CompatibleOption.builder()
                .isCompatible(compatibleOptionDto.isCompatible())
                .attributeOption(compatibleOptionDto.attributeOption())
                .categoryConfig(compatibleOptionDto.categoryConfig())
                .attribute(compatibleOptionDto.attribute())
                .build();
        compatibleOptionRepository.save(compatibleOption);
        return new GenericResponse(201, "compatible option created");
    }

    public GenericResponse setCompatibility(String id, Boolean compatible) {
        CompatibleOption compatability = compatibleOptionRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("could not find compatible option"));
        compatability.setIsCompatible(compatible);
        return new GenericResponse(201, "compatibility is "  + compatible);
    }

    public GenericResponse deleteAllCompatibleOptions() {
        compatibleOptionRepository.deleteAll();
        return new GenericResponse(201, "deleted all compatible options");
    }

    public GenericResponse deleteCompatibleOption(String id) {
        compatibleOptionRepository.deleteById(UUID.fromString(id));
        return new GenericResponse(201, "deleted compatible option");
    }
}
