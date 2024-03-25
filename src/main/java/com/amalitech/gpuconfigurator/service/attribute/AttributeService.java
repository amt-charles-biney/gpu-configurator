package com.amalitech.gpuconfigurator.service.attribute;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionGetResponse;
import com.amalitech.gpuconfigurator.exception.AttributeNameAlreadyExistsException;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface AttributeService {
    List<AttributeResponse> getAllAttributes();

    CompatibleOptionGetResponse getAllAttributeOptionsEditable();

    @Transactional
    List<AttributeResponse> createAttributeAndAttributeOptions(CreateAttributesRequest createAttributesRequest) throws AttributeNameAlreadyExistsException;

    @Transactional
    List<AttributeResponse> bulkUpdateAttributeAndAttributeOptions(UpdateAttributeDto updateAttributeDto);

    Attribute addAttribute(AttributeDto attribute) throws AttributeNameAlreadyExistsException;

    Attribute updateAttribute(UUID id, AttributeDto attribute);

    AttributeResponse getAttributeById(UUID id);

    GenericResponse deleteAttributeById(UUID attributeId);

    GenericResponse deleteAttributeOption(UUID attributeId, UUID optionId);

    void updateAllAttributeOptions(Attribute attribute, List<UpdateAttributeOptionDto> attributeOptionDtos);

    @Transactional
    List<AttributeOptionResponseDto> createAllAttributeOptions(UUID attributeId, @NotNull List<CreateAttributeOptionRequest> attributeOptionDtoList);

    GenericResponse deleteBulkAttributes(List<String> selectedAttributes);

    List<AttributeOption> getAllAttributeOptions();

    List<AttributeResponse> getAllAttributesLowInStock();

    List<Attribute> getAllAttributesByRequiredTrue();
}
