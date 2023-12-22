package com.amalitech.gpuconfigurator.dto.categoryconfig;

import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.model.category.CategoryConfig;
import lombok.Builder;

@Builder
public record CategoryConfigOptionDto(Category category, CategoryConfig categoryConfig, Attribute attribute, AttributeOption attributeOption) {
}
