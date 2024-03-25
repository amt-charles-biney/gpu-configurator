package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigOpt;
import lombok.Data;

import java.util.UUID;

@Data
public class ConfigUpdateDto {
    private String categoryId;
    private ConfigOpt options;
}
