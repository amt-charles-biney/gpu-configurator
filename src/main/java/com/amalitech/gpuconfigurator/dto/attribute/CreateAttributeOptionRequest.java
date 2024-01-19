package com.amalitech.gpuconfigurator.dto.attribute;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Builder
public record CreateAttributeOptionRequest(  @NotNull(message = "attribute option cannot be null")
                                             @NotBlank(message = "attribute option cannot blank")
                                             String name, BigDecimal price, String media, Float baseAmount, Float maxAmount, Double priceFactor) {
}

