package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.AttributeDto;
import com.amalitech.gpuconfigurator.model.Attribute;
import com.amalitech.gpuconfigurator.service.attribute.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AttributeController {

    private final AttributeService attributeService;

    @PostMapping("/v1/admin/attribute")
    public ResponseEntity<Attribute> createAttribute(@RequestBody AttributeDto atr) {
        Attribute attribute = attributeService.addAttribute(atr);
        return ResponseEntity.ok(attribute);
    }


}
