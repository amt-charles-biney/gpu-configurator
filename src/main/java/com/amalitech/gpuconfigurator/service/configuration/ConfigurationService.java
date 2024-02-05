package com.amalitech.gpuconfigurator.service.configuration;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;

public interface ConfigurationService {

    ConfigurationResponseDto saveConfiguration(String productId, Boolean warranty, String components);
}