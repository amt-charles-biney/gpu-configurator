package com.amalitech.gpuconfigurator.dto.attribute;

import jakarta.mail.Multipart;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record AttributeOptionDto(
        @NotNull(message = "attribute option cannot be null")
                @NotBlank(message = "attribute option cannot blank")
        String name, BigDecimal price, MultipartFile media, String unit, Float baseAmount, Float maxAmount, Float priceIncrement) {
}
