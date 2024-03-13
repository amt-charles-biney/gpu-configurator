package com.amalitech.gpuconfigurator.dto.categoryconfig;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllCategoryResponse {
    private UUID id;

    @NotNull(message = "category name is null")
    private String name;

    private String thumbnail;
}
