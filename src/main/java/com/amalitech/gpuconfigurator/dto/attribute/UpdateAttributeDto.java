package com.amalitech.gpuconfigurator.dto.attribute;

import java.util.List;

public record UpdateAttributeDto(String id, String attributeName, Boolean isMeasured, String description, String unit, List<UpdateAttributeOptionDto> variantOptions) {
}
