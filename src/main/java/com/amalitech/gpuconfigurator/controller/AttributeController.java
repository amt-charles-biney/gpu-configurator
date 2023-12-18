package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.AttributeDto;
import com.amalitech.gpuconfigurator.dto.AttributeOptionDto;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.model.Attribute;
import com.amalitech.gpuconfigurator.model.AttributeOption;
import com.amalitech.gpuconfigurator.service.attribute.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AttributeController {

    private final AttributeService attributeService;

    @PostMapping("/v1/admin/attribute")
    public ResponseEntity<Attribute> createAttribute(@RequestBody AttributeDto atr) {
        Attribute attribute = attributeService.addAttribute(atr);
        return ResponseEntity.status(HttpStatus.CREATED).body(attribute);
    }

    @GetMapping("/v1/admin/attribute")
    public ResponseEntity<List<Attribute>> getAttributes() {
        List<Attribute> attributes = attributeService.getAllAttributes();
        return ResponseEntity.ok(attributes);
    }

    @GetMapping("/v1/admin/attribute/{attributeId}")
    public ResponseEntity<Attribute> getAttribute(@PathVariable String attributeId) {
        Attribute attribute = attributeService.getAttributeById(UUID.fromString(attributeId));
        return ResponseEntity.ok(attribute);
    }

    @PutMapping("/v1/admin/attribute/{attributeId}")
    public ResponseEntity<Attribute> updateAttribute(@PathVariable String attributeId, @RequestBody  AttributeDto atr) {
        Attribute attribute = attributeService.updateAttribute(UUID.fromString(attributeId), atr);
        return ResponseEntity.status(HttpStatus.CREATED).body(attribute);
    }

    @DeleteMapping("/v1/admin/attribute/{attributeId}")
    public ResponseEntity<GenericResponse> deleteAttribute(@PathVariable String attributeId) {
        GenericResponse deleteResponse = attributeService.deleteAttributeById(UUID.fromString(attributeId));
        return ResponseEntity.ok(deleteResponse);
    }

    @PostMapping("/v1/admin/attribute/{attributeId}/option")
    public ResponseEntity<AttributeOption> createAttributeOption(@PathVariable String attributeId, @RequestBody AttributeOptionDto atrOption) {
        AttributeOption option = attributeService.createAttributeOption(UUID.fromString(attributeId), atrOption);
        return ResponseEntity.status(HttpStatus.CREATED).body(option);
    }



}
