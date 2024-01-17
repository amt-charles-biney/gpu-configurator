package com.amalitech.gpuconfigurator.service.configuration;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationRequestDto;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;



public interface ConfigurationService {

    ConfigurationResponseDto createConfiguration(ConfigurationRequestDto request);



    ConfigurationResponseDto configuration(String productId,String components,Boolean warranty);
}
