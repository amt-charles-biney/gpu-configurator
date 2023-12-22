package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.model.Attribute;
import com.amalitech.gpuconfigurator.model.AttributeOption;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.model.category.CategoryConfig;
import lombok.Builder;

@Builder
public record CategoryConfigOptionDto(Category category, CategoryConfig categoryConfig, Attribute attribute, AttributeOption attributeOption) {
}
