package com.amalitech.gpuconfigurator.dto.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigOpt {
    private String type;
    private String id;
}
