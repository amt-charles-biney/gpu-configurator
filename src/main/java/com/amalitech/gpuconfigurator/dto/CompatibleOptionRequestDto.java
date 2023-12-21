package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.model.Attribute;
import com.amalitech.gpuconfigurator.model.AttributeOption;

public record CompatibleOptionRequestDto(Boolean isCompatible, AttributeOption attributeOption, Attribute attribute) {
}
