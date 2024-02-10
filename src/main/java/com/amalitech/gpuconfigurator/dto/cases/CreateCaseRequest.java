package com.amalitech.gpuconfigurator.dto.cases;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CreateCaseRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private BigDecimal price;

    private List<UUID> incompatibleVariants = new ArrayList<>();
}