package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
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
            @RequestParam(required = false) String components,
            @RequestParam(required = false) Boolean warranty,
            @RequestParam(required = false) Boolean save
    ) {
        return configurationService.configuration(productId, components, warranty, save);
    }

}
