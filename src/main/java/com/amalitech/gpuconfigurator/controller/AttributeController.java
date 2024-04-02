package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.ApiResponse;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionEditResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionGetResponse;
import com.amalitech.gpuconfigurator.exception.AttributeNameAlreadyExistsException;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.service.attribute.AttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Attribute")
@RequiredArgsConstructor
@RequestMapping("/api")
public class AttributeController {

    private final AttributeService attributeService;

    @Operation(
            summary = "Create attributes and attribute options in bulk",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/admin/attributes/bulk")
    public ResponseEntity<ApiResponse<List<AttributeResponse>>> createAllAttributeandAttributeOptionsBulk(@Validated @RequestBody CreateAttributesRequest createAttributesRequest) throws AttributeNameAlreadyExistsException {
        List<AttributeResponse> attributeResponse = attributeService.createAttributeAndAttributeOptions(createAttributesRequest);
        ApiResponse<List<AttributeResponse>> attributeApiResponse = new ApiResponse<List<AttributeResponse>>(attributeResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(attributeApiResponse);
    }

    @Operation(
            summary = "Update attributes and attribute options in bulk",
            method = "PUT"
    )
    @CrossOrigin
    @PutMapping("/v1/admin/attributes/bulk")
    public ResponseEntity<ApiResponse<List<AttributeResponse>>> updateAllAttributendAttributeOptions(@Validated @RequestBody UpdateAttributeDto updateAttributeDto) {
        List<AttributeResponse> attributeResponse = attributeService.bulkUpdateAttributeAndAttributeOptions(updateAttributeDto);
        ApiResponse<List<AttributeResponse>> attributeApiResponse = new ApiResponse<List<AttributeResponse>>(attributeResponse);
        return ResponseEntity.ok(attributeApiResponse);
    }

    @Operation(
            summary = "Get attributes with pagination support",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/attributes/pageable")
    public ResponseEntity<Page<AttributeResponse>> getAttributesPageable(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String q
            ) {
        Page<AttributeResponse> attributes = attributeService.getAllAttributesPageable(size, page, q);
        return ResponseEntity.ok(attributes);
    }

    @Operation(
            summary = "Get all attributes",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/attributes")
    public ResponseEntity<ApiResponse<List<AttributeResponse>>> getAttributes() {
        List<AttributeResponse> attributes = attributeService.getAllAttributes();
        return ResponseEntity.ok(new ApiResponse<>(attributes));
    }

    @Operation(
            summary = "Get all editable attributes",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/attributes/config")
    public ResponseEntity<CompatibleOptionGetResponse> getAllAttributesEditable() {
        CompatibleOptionGetResponse compatibleOptionEditResponse = attributeService.getAllAttributeOptionsEditable();
        return ResponseEntity.ok(compatibleOptionEditResponse);
    }

    @Operation(
            summary = "Get attribute by ID",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/attributes/{attributeId}")
    public ResponseEntity<ApiResponse<AttributeResponse>> getAttribute(@PathVariable String attributeId) {
        AttributeResponse attribute = attributeService.getAttributeById(UUID.fromString(attributeId));
        ApiResponse<AttributeResponse> attributeResponse = new ApiResponse<AttributeResponse>(attribute);
        return ResponseEntity.ok(attributeResponse);
    }

    @Operation(
            summary = "Delete attribute by ID",
            method = "DELETE"
    )
    @CrossOrigin
    @DeleteMapping("/v1/admin/attributes/{attributeId}")
    public ResponseEntity<GenericResponse> deleteAttribute(@PathVariable String attributeId) {
        GenericResponse deleteResponse = attributeService.deleteAttributeById(UUID.fromString(attributeId));
        return ResponseEntity.ok(deleteResponse);
    }

    @Operation(
            summary = "Delete all attributes",
            method = "DELETE"
    )
    @CrossOrigin
    @DeleteMapping("/v1/admin/attributes/all")
    public ResponseEntity<GenericResponse> deleteAllAttributes(@RequestBody List<String> attributes) {
        GenericResponse deletedBulkAttributes = attributeService.deleteBulkAttributes(attributes);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedBulkAttributes);
    }

    @Operation(
            summary = "Delete attribute option",
            method = "DELETE"
    )
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
