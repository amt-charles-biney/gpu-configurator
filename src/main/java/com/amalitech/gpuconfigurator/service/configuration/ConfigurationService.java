package com.amalitech.gpuconfigurator.service.configuration;

import com.amalitech.gpuconfigurator.controller.testDto;
import com.amalitech.gpuconfigurator.dto.ConfigUpdateDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigResponseDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationRequestDto;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConfigurationService {

    ConfigurationResponseDto createConfiguration(ConfigurationRequestDto request);

}
