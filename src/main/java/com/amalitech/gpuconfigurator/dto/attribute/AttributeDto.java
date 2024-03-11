package com.amalitech.gpuconfigurator.dto.attribute;

import com.amalitech.gpuconfigurator.constant.AttributeErrors;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AttributeDto(
        @NotNull(message = AttributeErrors.ATTRIBUTE_NOT_NULL)
                @NotBlank(message = AttributeErrors.ATTRIBUTE_NOT_BLANK)
       String attributeName,

        @NotNull(message = AttributeErrors.ATTRIBUTE_NOT_NULL)
                @NotBlank(message = AttributeErrors.ATTRIBUTE_NOT_BLANK)
        Boolean isMeasured,
        @NotNull(message = AttributeErrors.ATTRIBUTE_NOT_NULL)
                @NotBlank(message = AttributeErrors.ATTRIBUTE_NOT_BLANK)
        String description,
        String unit,
        Boolean isRequired
) {
}
