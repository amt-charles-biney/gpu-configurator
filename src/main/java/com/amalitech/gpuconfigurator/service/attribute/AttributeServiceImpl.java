package com.amalitech.gpuconfigurator.service.attribute;

import com.amalitech.gpuconfigurator.dto.AttributeDto;
import com.amalitech.gpuconfigurator.dto.AttributeOptionDto;
import com.amalitech.gpuconfigurator.model.Attribute;
import com.amalitech.gpuconfigurator.model.AttributeOption;
import com.amalitech.gpuconfigurator.model.enums.AttributeType;
import com.amalitech.gpuconfigurator.repository.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.repository.AttributeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeOptionRepository attributeOptionRepository;

    @Override
    public List<Attribute> getAllAttributes() {
        return attributeRepository.findAll();
    }

    @Override
    public Attribute addAttribute(AttributeDto attribute) {
        var newAttribute =  Attribute.builder()
                .attributeName(attribute.name())
                .attributeType(AttributeType.valueOf(attribute.type()))
                .build();

        return attributeRepository.save(newAttribute);
    }

    @Override
    public Attribute updateAttribute(UUID id, AttributeDto attribute) {
        Attribute updateAttribute = attributeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("attribute not found"));

        updateAttribute.setAttributeName(attribute.name());
        updateAttribute.setAttributeType(AttributeType.valueOf(attribute.type()));

        return attributeRepository.save(updateAttribute);
   }

   @Override
   public Attribute getAttributeByName(String name) {
       return attributeRepository.findByAttributeName(name).orElseThrow(() -> new EntityNotFoundException("attribute not found"));
   }

    @Override
    public Attribute getAttributeById(UUID id) {
        return attributeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("attribute not found"));
    }


    @Override
    public void deleteAttributeByName(String name) {
        Attribute attribute = attributeRepository.findByAttributeName(name).orElseThrow(() -> new EntityNotFoundException("Attribute not found with name: " + name));
        attributeRepository.delete(attribute);
    }

    @Override
    public void deleteAttributeById(UUID attributeId) {
        Attribute attribute = attributeRepository.findById(attributeId).orElseThrow(() -> new EntityNotFoundException("Attribute not found with id: " + attributeId));
        attributeRepository.delete(attribute);
    }

    /* attribute options methodss */
    @Override
    public List<AttributeOption> getAllAttributeOptionByAttributeId(UUID id) {
        return attributeOptionRepository.findAllByAttributeId(id).orElseThrow(() -> new EntityNotFoundException("could not find attributes options"));
    }

    @Override
    public List<AttributeOption> getAllAttributeOptionByAttributeName(String name) {
        Attribute attributeByName = this.getAttributeByName(name);
        return attributeOptionRepository.findAllByAttributeId(UUID.fromString(attributeByName.getAttributeName())).orElseThrow(() -> new EntityNotFoundException("could not find attributes options"));
    }

    @Override
    public List<AttributeOption> getAllAttributeOptions() {
        return attributeOptionRepository.findAll();
    }

    @Override
    public AttributeOption getAttributeOptionById(UUID id) {
        return attributeOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("attribute with this id does not exist"));
    }

    @Override
    public void deleteAttributeOption(UUID id) {
        attributeOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("attribute with this id does not exist"));
        attributeOptionRepository.deleteById(id);
    }

    @Override
    public AttributeOption getAttributeOptionByName(String name) {
        return attributeOptionRepository.findByOptionName(name).orElseThrow(() -> new EntityNotFoundException("option by this name does not exist"));
    }

    @Override
    public void deleteAttributeOptionByName(String name){
        attributeOptionRepository.deleteByOptionName(name);
    }

    @Override
    public AttributeOption updateAttributeOption(UUID id, AttributeOptionDto atrDto) {
        AttributeOption updateAtr = attributeOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("attribute option does not exist"));
        updateAtr.setPriceAdjustment(atrDto.priceAdjustment());
        updateAtr.setOptionName(atrDto.optionName());

        return attributeOptionRepository.save(updateAtr);
    }

    @Override
    public AttributeOption createAttributeOption(UUID attributeId, AttributeOptionDto atr) {
        var attr = attributeRepository.findById(attributeId).orElseThrow(() -> new EntityNotFoundException("could not create option for this attribute type"));
        var newAttributeOption = AttributeOption.builder()
                .optionName(atr.optionName())
                .priceAdjustment(atr.priceAdjustment())
                .attribute(attr)
                .build();

        return attributeOptionRepository.save(newAttributeOption);
    }

}
