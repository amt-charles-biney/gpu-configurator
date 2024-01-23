package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.ApiResponse;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.exception.AttributeNameAlreadyExistsException;
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
    @PostMapping("/v1/admin/attributes/bulk")
    public ResponseEntity<ApiResponse<List<AttributeResponse>>> createAllAttributeandAttributeOptionsBulk(@Validated @RequestBody CreateAttributesRequest createAttributesRequest) throws AttributeNameAlreadyExistsException {
        List<AttributeResponse> attributeResponse = attributeService.createAttributeAndAttributeOptions(createAttributesRequest);
        ApiResponse<List<AttributeResponse>> attributeApiResponse = new ApiResponse<List<AttributeResponse>>(attributeResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(attributeApiResponse);
    }

    @CrossOrigin
    @PutMapping("/v1/admin/attributes/bulk")
    public ResponseEntity<ApiResponse<List<AttributeResponse>>> updateAllAttributendAttributeOptions(@Validated @RequestBody UpdateAttributeDto updateAttributeDto) {
        List<AttributeResponse> attributeResponse = attributeService.bulkUpdateAttributeAndAttributeOptions(updateAttributeDto);
        ApiResponse<List<AttributeResponse>> attributeApiResponse = new ApiResponse<List<AttributeResponse>>(attributeResponse);
        return ResponseEntity.ok(attributeApiResponse);
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
    @DeleteMapping("/v1/admin/attributes/{attributeId}")
    public ResponseEntity<GenericResponse> deleteAttribute(@PathVariable String attributeId) {
        GenericResponse deleteResponse = attributeService.deleteAttributeById(UUID.fromString(attributeId));
        return ResponseEntity.ok(deleteResponse);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/attributes/all")
    public ResponseEntity<GenericResponse> deleteAllAttributes(@RequestBody List<String> attributes) {
        GenericResponse deletedBulkAttributes = attributeService.deleteBulkAttributes(attributes);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedBulkAttributes);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/attributes/{attributeId}/options/{optionId}")
    public ResponseEntity<GenericResponse> deleteOption(
            @PathVariable String attributeId,
            @PathVariable String optionId
    ) {
        var response = attributeService.deleteAttributeOption(UUID.fromString(attributeId), UUID.fromString(optionId));
        return ResponseEntity.ok(response);
    }
}
