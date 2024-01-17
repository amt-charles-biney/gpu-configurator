package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigOpt;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class testDto {
    private String productId;
    private String categoryId;
    private ConfigOpt options;
}
