package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationRequestDto;
import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.service.configuration.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ConfigurationController {

    private final ConfigurationService configurationService;


    @CrossOrigin
    @PostMapping("/v1/configuration")
    @ResponseStatus(HttpStatus.CREATED)
    public ConfigurationResponseDto createConfiguration(@RequestBody ConfigurationRequestDto request) {
        return configurationService.createConfiguration(request);
    }


    @CrossOrigin
    @GetMapping("/v1/configuration")
    @ResponseStatus(HttpStatus.OK)
    public ConfigurationResponseDto configuration(
            @RequestParam(required = false) String productId,
            @RequestParam(required = false)UUID categoryId
            ){
        return configurationService.configuration(productId, categoryId);
    }

}
