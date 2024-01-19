package com.amalitech.gpuconfigurator.service.attribute;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.exception.AttributeNameAlreadyExistsException;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface AttributeService {
    List<AttributeResponse> getAllAttributes();

    @Transactional
    List<AttributeResponse> createAttributeAndAttributeOptions(CreateAttributesRequest createAttributesRequest) throws AttributeNameAlreadyExistsException;

    @Transactional
    List<AttributeResponse> bulkUpdateAttributeAndAttributeOptions(UpdateAttributeDto updateAttributeDto);

    Attribute addAttribute(AttributeDto attribute) throws AttributeNameAlreadyExistsException;

    AttributeResponse updateAttribute(UUID id, AttributeDto attribute);

    Attribute getAttributeByName(String name);

    AttributeResponse getAttributeById(UUID id);

    GenericResponse deleteAttributeByName(String name);

    GenericResponse deleteAttributeById(UUID attributeId);

    List<AttributeOptionResponseDto> getAllAttributeOptionByAttributeId(UUID id);

    List<AttributeOption> getAllAttributeOptionByAttributeName(String name);

    List<AttributeOptionResponseDto> getAllAttributeOptions();

    AttributeOptionResponseDto getAttributeOptionById(UUID id);

    GenericResponse deleteAttributeOption(UUID attributeId, UUID optionId);

    AttributeOption getAttributeOptionByName(String name);

    void deleteAttributeOptionByName(String name);

    AttributeOptionResponseDto updateAttributeOption(UUID id, AttributeOptionDto atrDto);

    void updateAllAttributeOptions(List<UpdateAttributeOptionDto> attributeOptionDtos);

    AttributeOptionResponseDto createAttributeOption(UUID attributeId, AttributeOptionDto atr);

    @Transactional
    List<AttributeOptionResponseDto> createAllAttributeOptions(UUID attributeId, @NotNull List<CreateAttributeOptionRequest> attributeOptionDtoList);

    GenericResponse deleteBulkAttributes(List<String> selectedAttributes);
}
