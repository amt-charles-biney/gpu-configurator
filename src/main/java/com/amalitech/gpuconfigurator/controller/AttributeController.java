package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.ApiResponse;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.service.attribute.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AttributeController {

    private final AttributeService attributeService;

    @CrossOrigin
    @PostMapping("/v1/admin/attributes")
    public ResponseEntity<ApiResponse<Attribute>> createAttribute(@Validated @RequestBody AttributeDto attribute) {
        Attribute attributeResponse = attributeService.addAttribute(attribute);
        ApiResponse<Attribute> attributeApiResponse = new ApiResponse<Attribute>(attributeResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(attributeApiResponse);
    }

    @CrossOrigin
    @PostMapping("/v1/admin/attributes/bulk")
    public ResponseEntity<ApiResponse<List<AttributeOptionResponseDto>>> createAllAttributeandAttributeOptionsBulk(@Validated @RequestBody CreateAttributesRequest createAttributesRequest) {
        List<AttributeOptionResponseDto> attributeResponse = attributeService.createAttributeAndAttributeOptions(createAttributesRequest);
        ApiResponse<List<AttributeOptionResponseDto>> attributeApiResponse = new ApiResponse<List<AttributeOptionResponseDto>>(attributeResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(attributeApiResponse);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/attributes")
    public ResponseEntity<ApiResponse<List<AttributeResponse>>> getAttributes() {
        List<AttributeResponse> attributes = attributeService.getAllAttributes();
        ApiResponse<List<AttributeResponse>> attributeResponse = new ApiResponse<List<AttributeResponse>>(attributes);
        return ResponseEntity.ok(attributeResponse);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/attributes/{attributeId}")
    public ResponseEntity<ApiResponse<AttributeResponse>> getAttribute(@PathVariable String attributeId) {
        AttributeResponse attribute = attributeService.getAttributeById(UUID.fromString(attributeId));
        ApiResponse<AttributeResponse> attributeResponse = new ApiResponse<AttributeResponse>(attribute);
        return ResponseEntity.ok(attributeResponse);
    }

    @CrossOrigin
    @PutMapping("/v1/admin/attributes/{attributeId}")
    public ResponseEntity<ApiResponse<AttributeResponse>> updateAttribute(@PathVariable String attributeId, @RequestBody  AttributeDto attribute) {
        AttributeResponse attributeDtoResponse = attributeService.updateAttribute(UUID.fromString(attributeId), attribute);
        ApiResponse<AttributeResponse> attributeResponse = new ApiResponse<AttributeResponse>(attributeDtoResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(attributeResponse);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/attributes/{attributeId}")
    public ResponseEntity<GenericResponse> deleteAttribute(@PathVariable String attributeId) {
        GenericResponse deleteResponse = attributeService.deleteAttributeById(UUID.fromString(attributeId));
        return ResponseEntity.ok(deleteResponse);
    }

    @CrossOrigin
    @PostMapping("/v1/admin/attributes/{attributeId}/options")
        public ResponseEntity<ApiResponse<AttributeOptionResponseDto>> createAttributeOption(@PathVariable String attributeId, @Validated @ModelAttribute AttributeOptionDto attributeOptionDto) {
        AttributeOptionResponseDto option = attributeService.createAttributeOption(UUID.fromString(attributeId), attributeOptionDto);
        ApiResponse<AttributeOptionResponseDto> optionResponse = new ApiResponse<AttributeOptionResponseDto>(option);
        return ResponseEntity.status(HttpStatus.CREATED).body(optionResponse);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/attributes/all")
    public ResponseEntity<GenericResponse> deleteAllAttributes(@RequestBody List<String> attributes) {
        GenericResponse deletedBulkAttributes = attributeService.deleteBulkAttributes(attributes);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedBulkAttributes);
    }


    @CrossOrigin
    @GetMapping("/v1/admin/attributes/options")
    public ResponseEntity<ApiResponse<List<AttributeOptionResponseDto>>> getAllOptions() {
         List<AttributeOptionResponseDto> attributeOptions = attributeService.getAllAttributeOptions();
         ApiResponse<List<AttributeOptionResponseDto>> attributeOptionsResponse = new ApiResponse<List<AttributeOptionResponseDto>>(attributeOptions);
         return ResponseEntity.ok(attributeOptionsResponse);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/attributes/{attributeId}/options")
    public ResponseEntity<ApiResponse<List<AttributeOptionResponseDto>>> getAllAttributeOptions(@PathVariable String attributeId) {
        List<AttributeOptionResponseDto> attributeOptions = attributeService.getAllAttributeOptionByAttributeId(UUID.fromString(attributeId));
        ApiResponse<List<AttributeOptionResponseDto>> attributeOptionsResponse = new ApiResponse<List<AttributeOptionResponseDto>>(attributeOptions);
        return ResponseEntity.ok(attributeOptionsResponse);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/attributes/options/{optionId}")
    public ResponseEntity<ApiResponse<AttributeOptionResponseDto>> getOption(@PathVariable String optionId) {
        AttributeOptionResponseDto attributeOption = attributeService.getAttributeOptionById(UUID.fromString(optionId));
        ApiResponse<AttributeOptionResponseDto> attributeOptionResponse = new ApiResponse<AttributeOptionResponseDto>(attributeOption);
        return ResponseEntity.ok(attributeOptionResponse);
    }
    @CrossOrigin
    @PutMapping("/v1/admin/attributes/options/{optionId}")
    public ResponseEntity<ApiResponse<AttributeOptionResponseDto>> updateOption(@PathVariable String optionId, @ModelAttribute AttributeOptionDto attributeOptionDto) {
        AttributeOptionResponseDto attributeOption = attributeService.updateAttributeOption(UUID.fromString(optionId), attributeOptionDto);
        ApiResponse<AttributeOptionResponseDto> attributeOptionResponse = new ApiResponse<AttributeOptionResponseDto>(attributeOption);
        return ResponseEntity.ok(attributeOptionResponse);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/attributes/options/{optionId}")
    public ResponseEntity<GenericResponse> deleteOption(@PathVariable String optionId) {
        GenericResponse attributeOption = attributeService.deleteAttributeOption(UUID.fromString(optionId));
        return ResponseEntity.ok(attributeOption);
    }
}
