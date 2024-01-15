package com.amalitech.gpuconfigurator.service.attribute;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeOptionRepository attributeOptionRepository;

    @Override
    public List<AttributeResponse> getAllAttributes() {
        List<Attribute> attributes = attributeRepository.findAll();

        return attributes.stream()
                .map(this::createAttributeResponseType)
                .collect(Collectors.toList());
    }

    @Override
    public Attribute addAttribute(@NotNull AttributeDto attribute) {
        Attribute newAttribute =  Attribute.builder()
                .attributeName(attribute.attributeName())
                .build();

        return attributeRepository.save(newAttribute);
    }

    @Override
    public AttributeResponse updateAttribute(UUID id, @NotNull AttributeDto attribute) {
        Attribute updateAttribute = attributeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));

        updateAttribute.setAttributeName(attribute.attributeName());
        updateAttribute.setUpdatedAt(LocalDateTime.now());

        Attribute savedAttribute = attributeRepository.save(updateAttribute);
        return this.createAttributeResponseType(savedAttribute);
   }

   @Override
   public Attribute getAttributeByName(String name) {
       return attributeRepository.findByAttributeName(name).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));
   }

    @Override
    public AttributeResponse getAttributeById(UUID id) {
        Attribute attribute = attributeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));
        return this.createAttributeResponseType(attribute);
    }

    @Override
    public void deleteAttributeByName(String name) {
        Attribute attribute = attributeRepository.findByAttributeName(name).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST + name));
        attributeRepository.delete(attribute);
    }

    @Override
    public GenericResponse deleteAttributeById(UUID attributeId) {
        Attribute attribute = attributeRepository.findById(attributeId).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST +  attributeId));
        attributeRepository.delete(attribute);

        return GenericResponse.builder().status(HttpStatus.ACCEPTED.value()).message("deleted attribute successfully").build();
    }

    @Override
    public List<AttributeOptionResponseDto> getAllAttributeOptionByAttributeId(UUID id) {
        List<AttributeOption> attr =  attributeOptionRepository.findAllByAttributeId(id).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_OPTIONS_NOT_EXIST));
        return this.streamAttributeOptions(attr);
    }


    @Override
    public List<AttributeOption> getAllAttributeOptionByAttributeName(String name) {
        Attribute attributeByName = this.getAttributeByName(name);
        return attributeOptionRepository.findAllByAttributeId(UUID.fromString(attributeByName.getAttributeName())).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_OPTIONS_NOT_EXIST));
    }

    @Override
    public List<AttributeOptionResponseDto> getAllAttributeOptions() {
        List<AttributeOption> attributeOptions = attributeOptionRepository.findAll();
        return this.streamAttributeOptions(attributeOptions);
    }

    @Override
    public AttributeOptionResponseDto getAttributeOptionById(UUID id) {
        AttributeOption attribute = attributeOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));
        return this.createAttributeResponseType(attribute);
    }

    @Override
    public GenericResponse deleteAttributeOption(UUID id) {
        attributeOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));
        attributeOptionRepository.deleteById(id);

        return GenericResponse.builder().status(HttpStatus.ACCEPTED.value()).message("deleted attribute option successfully").build();
    }

    @Override
    public AttributeOption getAttributeOptionByName(String name) {
        return attributeOptionRepository.findByOptionName(name).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_OPTION_NOT_EXIST));
    }

    @Override
    public void deleteAttributeOptionByName(String name){ attributeOptionRepository.deleteByOptionName(name); }

    @Override
    public AttributeOptionResponseDto updateAttributeOption(UUID id, @NotNull AttributeOptionDto attributeOptionDto) {
        AttributeOption updateAtr = attributeOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_OPTION_NOT_EXIST));

        updateAtr.setPriceAdjustment(attributeOptionDto.price());
        updateAtr.setOptionName(attributeOptionDto.name());
        updateAtr.setMedia(attributeOptionDto.media());
        updateAtr.setUnit(attributeOptionDto.unit());
        updateAtr.setUpdatedAt(LocalDateTime.now());

        AttributeOption savedAttribute =  attributeOptionRepository.save(updateAtr);
        return this.createAttributeResponseType(savedAttribute);
    }

    @Override
    public AttributeOptionResponseDto createAttributeOption(UUID attributeId, @NotNull AttributeOptionDto attributeOptionDtoResponse) {
        var attribute = attributeRepository.findById(attributeId).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));
        var newAttributeOption = AttributeOption.builder()
                .optionName(attributeOptionDtoResponse.name())
                .priceAdjustment(attributeOptionDtoResponse.price())
                .media(attributeOptionDtoResponse.media())
                .unit(attributeOptionDtoResponse.unit())
                .attribute(attribute)
                .build();

        AttributeOption savedAttribute = attributeOptionRepository.save(newAttributeOption);
        return this.createAttributeResponseType(savedAttribute);
    }



    private List<AttributeOptionResponseDto> streamAttributeOptions(@NotNull List<AttributeOption> instance) {
        return instance
                .stream()
                .map(this::createAttributeResponseType)
                .collect(Collectors.toList());
    }

    private AttributeOptionResponseDto createAttributeResponseType(@NotNull AttributeOption attributeOption) {
        return AttributeOptionResponseDto
                .builder()
                .id(attributeOption.getId().toString())
                .optionName(attributeOption.getOptionName())
                .optionPrice(attributeOption.getPriceAdjustment())
                .optionUnit(attributeOption.getUnit())
                .optionMedia(attributeOption.getMedia())
                .attribute(new AttributeResponseDto(attributeOption.getAttribute().getAttributeName(), attributeOption.getAttribute().getId().toString()))
                .build();
    }

    private AttributeResponse createAttributeResponseType(@NotNull Attribute attribute) {
        List<AttributeOption> attributeOptions = attributeOptionRepository.findAllByAttributeId(attribute.getId()).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_OPTIONS_NOT_EXIST));
        return AttributeResponse
                .builder()
                .id(attribute.getId())
                .attributeName(attribute.getAttributeName())
                .attributeOptions(this.streamAttributeOptions(attributeOptions))
                .build();
    }


}