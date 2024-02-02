package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.service.configuration.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @CrossOrigin
    @GetMapping("/v1/config/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ConfigurationResponseDto configuration(
            @PathVariable("productId") String productId,
            @RequestParam(required = false) Boolean warranty,
            @RequestParam(required = false) String components
    ) {
        return configurationService.configuration(productId, warranty, components);
    }

    @CrossOrigin
    @PostMapping("/v1/config/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ConfigurationResponseDto createConfiguration(
            @PathVariable("productId") String productId,
            @RequestParam(required = false) Boolean warranty,
            @RequestParam(required = false) String components
    ) {
        return configurationService.saveConfiguration(productId, warranty, components);
    }

    @CrossOrigin
    @GetMapping("/v1/config/one/{configId}")
    @ResponseStatus(HttpStatus.OK)
    public Configuration getOneConfiguration(@PathVariable("configId") String configId) {
        return configurationService.getSpecificConfiguration(configId);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/config/one/{configId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteConfiguration(@PathVariable("configId") String configId) {
         configurationService.deleteSpecificConfiguration(configId);
    }

}
