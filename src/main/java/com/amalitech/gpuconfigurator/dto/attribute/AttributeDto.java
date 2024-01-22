package com.amalitech.gpuconfigurator.dto.attribute;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AttributeDto(
        @NotNull(message = AttributeConstant.ATTRIBUTE_NOT_NULL)
                @NotBlank(message = AttributeConstant.ATTRIBUTE_NOT_BLANK)
       String attributeName,

        @NotNull(message = AttributeConstant.ATTRIBUTE_NOT_NULL)
                @NotBlank(message = AttributeConstant.ATTRIBUTE_NOT_BLANK)
        Boolean isMeasured,
        @NotNull(message = AttributeConstant.ATTRIBUTE_NOT_NULL)
                @NotBlank(message = AttributeConstant.ATTRIBUTE_NOT_BLANK)
        String description,
        String unit
) {
}
