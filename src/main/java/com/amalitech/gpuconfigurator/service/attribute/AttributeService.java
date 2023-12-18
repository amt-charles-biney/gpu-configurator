package com.amalitech.gpuconfigurator.service.attribute;

import com.amalitech.gpuconfigurator.dto.AttributeDto;
import com.amalitech.gpuconfigurator.dto.AttributeOptionDto;
import com.amalitech.gpuconfigurator.model.Attribute;
import com.amalitech.gpuconfigurator.model.AttributeOption;

import java.util.List;
import java.util.UUID;

public interface AttributeService {
    List<Attribute> getAllAttributes();

    Attribute addAttribute(AttributeDto attribute);

    Attribute updateAttribute(UUID id, AttributeDto attribute);

    Attribute getAttributeByName(String name);

    Attribute getAttributeById(UUID id);

    void deleteAttributeByName(String name);

    void deleteAttributeById(UUID attributeId);

    List<AttributeOption> getAllAttributeOptionByAttributeId(UUID id);

    List<AttributeOption> getAllAttributeOptionByAttributeName(String name);

    List<AttributeOption> getAllAttributeOptions();

    AttributeOption getAttributeOptionById(UUID id);

    void deleteAttributeOption(UUID id);

    AttributeOption getAttributeOptionByName(String name);

    void deleteAttributeOptionByName(String name);

    AttributeOption updateAttributeOption(UUID id, AttributeOptionDto atrDto);

    AttributeOption createAttributeOption(UUID attributeId, AttributeOptionDto atr);
}
