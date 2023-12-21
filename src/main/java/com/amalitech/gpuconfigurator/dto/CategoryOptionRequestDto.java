package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.model.Attribute;
import com.amalitech.gpuconfigurator.model.AttributeOption;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.model.category.CategoryConfig;

public record CategoryOptionRequestDto(Attribute attribute, AttributeOption attributeOption) {
}
