package com.amalitech.gpuconfigurator.service.attribute;

import com.amalitech.gpuconfigurator.dto.*;
import com.amalitech.gpuconfigurator.model.Attribute;
import com.amalitech.gpuconfigurator.model.AttributeOption;

import java.util.List;
import java.util.UUID;

public interface AttributeService {
    List<AttributeResponse> getAllAttributes();

    Attribute addAttribute(AttributeDto attribute);

    AttributeResponse updateAttribute(UUID id, AttributeDto attribute);

    Attribute getAttributeByName(String name);

    AttributeResponse getAttributeById(UUID id);

    void deleteAttributeByName(String name);

    GenericResponse deleteAttributeById(UUID attributeId);

    List<AttributeOptionResponseDto> getAllAttributeOptionByAttributeId(UUID id);

    List<AttributeOption> getAllAttributeOptionByAttributeName(String name);

    List<AttributeOptionResponseDto> getAllAttributeOptions();

    AttributeOptionResponseDto getAttributeOptionById(UUID id);

    GenericResponse deleteAttributeOption(UUID id);

    AttributeOption getAttributeOptionByName(String name);

    void deleteAttributeOptionByName(String name);

    AttributeOptionResponseDto updateAttributeOption(UUID id, AttributeOptionDto atrDto);

    AttributeOptionResponseDto createAttributeOption(UUID attributeId, AttributeOptionDto atr);
}
