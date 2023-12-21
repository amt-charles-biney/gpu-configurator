package com.amalitech.gpuconfigurator.service.category.CategoryConfig;

import com.amalitech.gpuconfigurator.dto.CategoryConfigOptionDto;
import com.amalitech.gpuconfigurator.dto.CompatibleOptionDto;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.model.Attribute;
import com.amalitech.gpuconfigurator.model.AttributeOption;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.model.category.CategoryConfigOption;
import com.amalitech.gpuconfigurator.model.category.CompatibleOption;
import com.amalitech.gpuconfigurator.repository.category.CategoryConfigOptionRepository;
import com.amalitech.gpuconfigurator.repository.category.CategoryConfigRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryConfigOptionService {

    private final CategoryConfigOptionRepository categoryConfigOptionRepository;

    @Transactional
    public void addBulkCategoryConfigOptions(List<CategoryConfigOptionDto> categoryConfigOpt) {
        List<CategoryConfigOption> categoryConfigOptions = categoryConfigOpt
                .stream()
                .map(option ->
                        CategoryConfigOption.builder()
                                .categoryConfig(option.categoryConfig())
                                .attribute(option.attribute())
                                .category(option.category())
                                .attributeOption(option.attributeOption())
                                .build()
                ).collect(Collectors.toList());

        categoryConfigOptionRepository.saveAll(categoryConfigOptions);
    }

    public GenericResponse deleteCategoryOption(String id) {
        categoryConfigOptionRepository.deleteById(UUID.fromString(id));
        return new GenericResponse(201, "deleted category option successfully");
    }

    public GenericResponse changeCategoryOption(String id, AttributeOption attributeOption) {
        CategoryConfigOption categoryConfigOption = categoryConfigOptionRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("could not find category"));
        categoryConfigOption.setAttributeOption(attributeOption);
        return new GenericResponse(201, "category option updated successfully");
    }
}
