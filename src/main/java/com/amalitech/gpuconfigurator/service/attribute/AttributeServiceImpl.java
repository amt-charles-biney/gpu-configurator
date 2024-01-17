package com.amalitech.gpuconfigurator.service.attribute;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeRepository;
import com.amalitech.gpuconfigurator.service.cloudinary.UploadImageServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeOptionRepository attributeOptionRepository;
    private final UploadImageServiceImpl cloudinaryUploadService;

    @Override
    public List<AttributeResponse> getAllAttributes() {
        List<Attribute> attributes = attributeRepository.findAll();

        return attributes.stream()
                .map(this::createAttributeResponseType)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AttributeResponse createAttributeAndAttributeOptions(CreateAttributesRequest createAttributesRequest) {
        Attribute createAttribute = this.addAttribute(new AttributeDto(createAttributesRequest.attributeName(), createAttributesRequest.isMeasured(), createAttributesRequest.description(), createAttributesRequest.unit()));
        List<AttributeOptionResponseDto> attributeResponse = this.createAllAttributeOptions(createAttribute.getId(), createAttributesRequest.variantOptions());

        return this.createAttributeResponseType(createAttribute);
    }

    @Override
    public Attribute addAttribute(@NotNull AttributeDto attribute) {
        Attribute newAttribute =  Attribute.builder()
                .attributeName(attribute.attributeName())
                .isMeasured(attribute.isMeasured())
                .description(attribute.description())
                .unit(attribute.unit())
                .build();

        return attributeRepository.save(newAttribute);
    }

    @Override
    public AttributeResponse updateAttribute(UUID id, @NotNull AttributeDto attribute) {
        Attribute updateAttribute = attributeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));

        updateAttribute.setAttributeName(attribute.attributeName());
        updateAttribute.setMeasured(attribute.isMeasured());
        updateAttribute.setDescription(attribute.description());
        updateAttribute.setUnit(attribute.unit());
        updateAttribute.setUpdatedAt(LocalDateTime.now());

        Attribute savedAttribute = attributeRepository.save(updateAttribute);
        return this.createAttributeResponseType(savedAttribute);
   }

   @Override
   public Attribute getAttributeByName(String name) {
       return attributeRepository.findByAttributeName(name)
               .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));
   }

    @Override
    public AttributeResponse getAttributeById(UUID id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));

        return this.createAttributeResponseType(attribute);
    }

    @Override
    public GenericResponse deleteAttributeByName(String name) {
        Attribute attribute = attributeRepository.findByAttributeName(name)
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST + name));
        attributeRepository.delete(attribute);
        return new GenericResponse(HttpStatus.ACCEPTED.value(), "deleted attribute by name successful");
    }

    @Override
    public GenericResponse deleteAttributeById(UUID attributeId) {
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST +  attributeId));
        attributeRepository.delete(attribute);

        return new GenericResponse(HttpStatus.ACCEPTED.value(),"deleted attribute successfully");
    }

    @Override
    public List<AttributeOptionResponseDto> getAllAttributeOptionByAttributeId(UUID id) {
        List<AttributeOption> attr =  attributeOptionRepository.findAllByAttributeId(id)
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_OPTIONS_NOT_EXIST));
        return this.streamAttributeOptions(attr);
    }


    @Override
    public List<AttributeOption> getAllAttributeOptionByAttributeName(String name) {
        Attribute attributeByName = this.getAttributeByName(name);
        return attributeOptionRepository.findAllByAttributeId(UUID.fromString(attributeByName.getAttributeName()))
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_OPTIONS_NOT_EXIST));
    }

    @Override
    public List<AttributeOptionResponseDto> getAllAttributeOptions() {
        List<AttributeOption> attributeOptions = attributeOptionRepository.findAll();
        return this.streamAttributeOptions(attributeOptions);
    }

    @Override
    public AttributeOptionResponseDto getAttributeOptionById(UUID id) {
        AttributeOption attribute = attributeOptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));
        return this.createAttributeResponseType(attribute);
    }

    @Override
    public GenericResponse deleteAttributeOption(UUID id) {
        attributeOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));
        attributeOptionRepository.deleteById(id);
        return GenericResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("deleted attribute option successfully")
                .build();
    }

    @Override
    public AttributeOption getAttributeOptionByName(String name) {
        return attributeOptionRepository.findByOptionName(name)
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_OPTION_NOT_EXIST));
    }

    @Override
    public void deleteAttributeOptionByName(String name){ attributeOptionRepository.deleteByOptionName(name); }

    @Override
    public AttributeOptionResponseDto updateAttributeOption(UUID id, @NotNull AttributeOptionDto attributeOptionDto) {
        AttributeOption updateAtr = attributeOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_OPTION_NOT_EXIST));

        updateAtr.setPriceAdjustment(attributeOptionDto.price());
        updateAtr.setOptionName(attributeOptionDto.name());
        updateAtr.setBaseAmount(attributeOptionDto.baseAmount());
        updateAtr.setMaxAmount(attributeOptionDto.maxAmount());
        updateAtr.setPriceIncrement(attributeOptionDto.priceIncrement());
        updateAtr.setUpdatedAt(LocalDateTime.now());

        AttributeOption savedAttribute =  attributeOptionRepository.save(updateAtr);
        return this.createAttributeResponseType(savedAttribute);
    }

    @Override
    public AttributeOptionResponseDto createAttributeOption(UUID attributeId, @NotNull AttributeOptionDto attributeOptionDtoResponse) {
        var attribute = attributeRepository.findById(attributeId).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));

        MultipartFile media = attributeOptionDtoResponse.media();


        var newAttributeOption = AttributeOption.builder()
                .optionName(attributeOptionDtoResponse.name())
                .priceAdjustment(attributeOptionDtoResponse.price())
                .attribute(attribute)
                .build();

        if(!media.isEmpty()) {
            String getCloudinaryUrl = cloudinaryUploadService.uploadCoverImage(media);
            newAttributeOption.setMedia(getCloudinaryUrl);
        }

        if(attribute.isMeasured()) {
            newAttributeOption.setBaseAmount(attributeOptionDtoResponse.baseAmount());
            newAttributeOption.setMaxAmount(attributeOptionDtoResponse.maxAmount());
            newAttributeOption.setPriceIncrement(attributeOptionDtoResponse.priceIncrement());
        }

        AttributeOption savedAttribute = attributeOptionRepository.save(newAttributeOption);
        return this.createAttributeResponseType(savedAttribute);
    }

    @Override
    @Transactional
    public List<AttributeOptionResponseDto> createAllAttributeOptions(UUID attributeId, @NotNull List<CreateAttributeOptionRequest> attributeOptionDtoList) {
        Attribute attribute = attributeRepository.findById(attributeId).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));
        List<AttributeOption> attributeOptionList = attributeOptionDtoList.stream().map(attributes -> AttributeOption.builder()
                .optionName(attributes.name())
                .priceAdjustment(attributes.price())
                .attribute(attribute)
                .media(attributes.media())
                .baseAmount(attributes.baseAmount())
                .maxAmount(attributes.maxAmount())
                .priceIncrement(attributes.priceIncrement())
                .build()).toList();

        List<AttributeOption> savedAttributeOptions = attributeOptionRepository.saveAll(attributeOptionList);
        return savedAttributeOptions.stream().map(this::createAttributeResponseType).toList();
    }

    @Override
    public GenericResponse deleteBulkAttributes(List<String> selectedAttributes){
        List<UUID> selectedAttributesUUID = selectedAttributes.stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());

        attributeRepository.deleteAllById(selectedAttributesUUID);
        return new GenericResponse(HttpStatus.ACCEPTED.value(),"deleted bulk attributes successful");
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
                .additionalInfo(new AttributeVariantDto(attributeOption.getBaseAmount(), attributeOption.getMaxAmount(), attributeOption.getPriceIncrement()))
                .optionMedia(attributeOption.getMedia())
                .attribute(
                        new AttributeResponseDto(
                        attributeOption.getAttribute().getAttributeName(),
                        attributeOption.getAttribute().getId().toString(),
                        attributeOption.getAttribute().isMeasured()))
                .build();
    }

    private AttributeResponse createAttributeResponseType(@NotNull Attribute attribute) {
        List<AttributeOption> attributeOptions = attributeOptionRepository.findAllByAttributeId(attribute.getId()).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_OPTIONS_NOT_EXIST));
        return AttributeResponse
                .builder()
                .id(attribute.getId())
                .attributeName(attribute.getAttributeName())
                .isMeasured(attribute.isMeasured())
                .description(attribute.getDescription())
                .unit(attribute.getUnit())
                .attributeOptions(this.streamAttributeOptions(attributeOptions))
                .build();
    }
}
