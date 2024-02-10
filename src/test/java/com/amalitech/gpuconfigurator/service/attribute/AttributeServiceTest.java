package com.amalitech.gpuconfigurator.service.attribute;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionEditResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionGetResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.exception.AttributeNameAlreadyExistsException;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @Mock
    private AttributeRepository attributeRepository;
    @Mock
    private AttributeOptionRepository attributeOptionRepository;

    @InjectMocks
    private AttributeServiceImpl attributeService;

    private AttributeDto attributeDto;
    private Attribute attribute;
    private UUID attributeId;

    private AttributeOption attributeOption;

    @BeforeEach
    void Setup() {
        attributeId = UUID.randomUUID();
        attributeDto = AttributeDto.builder()
                .attributeName("Disk")
                .isMeasured(true)
                .description("new disk")
                .unit("gb")
                .build();

        attribute = Attribute.builder()
                .id(attributeId)
                .attributeName(attributeDto.attributeName())
                .description(attributeDto.description())
                .description(attributeDto.description())
                .unit(attributeDto.unit())
                .build();

        attributeOption =  AttributeOption
                .builder()
                .optionName("sample")
                .media("/http.png")
                .id(UUID.randomUUID())
                .priceAdjustment(BigDecimal.valueOf(0))
                .priceFactor(1.4)
                .baseAmount(23F)
                .maxAmount(24F)
                .attribute(attribute)
                .build();
    }

    @Test
    void testAddAttribute_shouldBeAbleToAdd() {
        when(attributeRepository.save(any(Attribute.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, Attribute.class));

        Attribute savedAttribute = attributeService.addAttribute(attributeDto);

        assertNotNull(savedAttribute);
        assertEquals(savedAttribute.getAttributeName(), attribute.getAttributeName());
    }

    @Test
    void testAddAttribute_DuplicateName_shouldThrowDataIntegrityError() {
        when(attributeRepository.existsByAttributeName("Disk"))
                .thenReturn(true);

        assertThrows(AttributeNameAlreadyExistsException.class, () -> attributeService.addAttribute(attributeDto));

        verify(attributeRepository, times(1)).existsByAttributeName("Disk");
        verify(attributeRepository, never()).save(any());

    }

    @Test
    public void testDeleteAttributeById_shouldBeAbleToDelete() {
        Attribute attribute = new Attribute(); // create an instance of your Attribute
        attribute.setId(attributeId);

        when(attributeRepository.findById(attributeId)).thenReturn(Optional.of(attribute));
        GenericResponse response = attributeService.deleteAttributeById(attributeId);

        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED.value(), response.status());
        assertEquals("deleted attribute successfully", response.message());

        verify(attributeRepository, times(1)).findById(attributeId);
        verify(attributeRepository, times(1)).delete(attribute);
    }

    @Test
    public void testDeleteAttributeById_EntityNotFound() {
        when(attributeRepository.findById(attributeId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> attributeService.deleteAttributeById(attributeId));

        verify(attributeRepository, times(1)).findById(attributeId);
        verify(attributeRepository, never()).delete(any());
    }

    @Test
    public void testDeleteBulkAttributes() {
        List<String> selectedAttributes = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        List<UUID> selectedAttributesUUID = selectedAttributes.stream()
                .map(UUID::fromString)
                .toList();

        GenericResponse response = attributeService.deleteBulkAttributes(selectedAttributes);

        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED.value(), response.status());
        assertEquals("deleted bulk attributes successful", response.message());

        verify(attributeRepository, times(1)).deleteAllById(selectedAttributesUUID);
    }

    @Test
    public void testCreateAllAttributeOptions_shouldCreateAttributeOptions() {
        UUID attributeId = UUID.randomUUID();
        attribute.setId(attributeId);

        List<CreateAttributeOptionRequest> attributeOptionDtoList = List.of(
                new CreateAttributeOptionRequest("Option1", BigDecimal.valueOf(10.0), "Media1", 50.0f, 100.0f, 1.2, "nvidea", new ArrayList<>()),
                new CreateAttributeOptionRequest("Option2", BigDecimal.valueOf(20.0), "Media2", 60.0f, 120.0f, 1.5, "nvidea", new ArrayList<>())
        );

        List<AttributeOption> attributeOptionList = attributeOptionDtoList.stream()
                .map(attributes -> AttributeOption.builder()
                        .optionName(attributes.name())
                        .priceAdjustment(attributes.price())
                        .attribute(attribute)
                        .media(attributes.media())
                        .baseAmount(attributes.baseAmount())
                        .incompatibleAttributeOptions(attributeOption.getIncompatibleAttributeOptions())
                        .brand("nvidea")
                        .maxAmount(attributes.maxAmount())
                        .priceFactor(attributes.priceFactor())
                        .build())
                .toList();

        when(attributeRepository.findById(attributeId)).thenReturn(Optional.of(attribute));
        when(attributeOptionRepository.saveAll(anyList())).thenAnswer(invocation -> {

            List<AttributeOption> inputList = invocation.getArgument(0);

            List<AttributeOption> savedList = inputList.stream()
                    .map(attributeOption -> AttributeOption.builder()
                            .id(UUID.randomUUID())
                            .optionName(attributeOption.getOptionName())
                            .priceAdjustment(attributeOption.getPriceAdjustment())
                            .attribute(attributeOption.getAttribute())
                            .media(attributeOption.getMedia())
                            .incompatibleAttributeOptions(attributeOption.getIncompatibleAttributeOptions())
                            .brand("nvidea")
                            .baseAmount(attributeOption.getBaseAmount())
                            .maxAmount(attributeOption.getMaxAmount())
                            .priceFactor(attributeOption.getPriceFactor())
                            .build())
                    .toList();

            return savedList;
        });

        List<AttributeOptionResponseDto> response = attributeService.createAllAttributeOptions(attributeId, attributeOptionDtoList);

        assertNotNull(response);
        assertEquals(2, response.size());

        verify(attributeRepository, times(1)).findById(attributeId);
        verify(attributeOptionRepository, times(1)).saveAll(anyList());

    }

    @Test
    public void testUpdateAllAttributeOptions() {
        UUID attribute1 = UUID.randomUUID();
        UUID attribute2 = UUID.randomUUID();
        List<UpdateAttributeOptionDto> attributeOptionDtos = List.of(
                new UpdateAttributeOptionDto(attribute1.toString(), "Option1", BigDecimal.valueOf(15.0), "nvidea", new ArrayList<>(), "updatedMedia1", 120.0f, 130.0f, 1.5),
                new UpdateAttributeOptionDto(attribute2.toString(), "Option2", BigDecimal.valueOf(25.0), "nvidea", new ArrayList<>(), "updatedMedia2", 130.0f, 12.0f, 1.5)
        );

        when(attributeOptionRepository.findById(attribute1))
                .thenReturn(Optional.of(new AttributeOption()));
        when(attributeOptionRepository.findById(attribute2))
                .thenReturn(Optional.of(new AttributeOption()));

        when(attributeOptionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        attributeService.updateAllAttributeOptions(attribute, attributeOptionDtos);

        verify(attributeOptionRepository, times(2)).findById(any(UUID.class));
        verify(attributeOptionRepository, times(2)).save(any(AttributeOption.class));
    }

    @Test
    void testGetAttributeById_shouldReturnAttribute() {
        attribute.setId(attributeId);

        List<AttributeOption> attributeOptions = List.of(
                AttributeOption.builder()
                        .id(UUID.randomUUID())
                        .optionName("Option 1")
                        .attribute(attribute)
                        .incompatibleAttributeOptions(new ArrayList<>())
                        .build()
        );

        when(attributeRepository.findById(attributeId)).thenReturn(Optional.of(attribute));
        when(attributeOptionRepository.findAllByAttributeId(attributeId)).thenReturn(Optional.of(attributeOptions));

        AttributeResponse returnedAttribute = attributeService.getAttributeById(attributeId);

        assertEquals(attributeId, returnedAttribute.id());
        assertEquals(attribute.getAttributeName(), returnedAttribute.attributeName());
        assertEquals(attribute.getDescription(), returnedAttribute.description());

        verify(attributeRepository, times(1)).findById(attributeId);
        verify(attributeOptionRepository, times(1)).findAllByAttributeId(attributeId);
    }


    @Test
    void testGetAttributeById_invalidID_shouldThrowEntityNotFoundException() {

        when(attributeRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> attributeService.getAttributeById(attributeId));

        verify(attributeRepository, times(1)).findById(attributeId);
    }

    @Test
    public void testUpdateAttribute_Success() {
        attribute.setId(attributeId);
        AttributeDto attributeDto = new AttributeDto("updated", true, "UpdatedDescription", "TB", true);

        List<AttributeOption> attributeOptions = List.of(
                AttributeOption.builder().id(UUID.randomUUID()).optionName("Option 1").attribute(attribute).build()
        );

        when(attributeRepository.findById(attributeId)).thenReturn(Optional.of(attribute));
        when(attributeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Attribute updatedAttribute = attributeService.updateAttribute(attributeId, attributeDto);

        assertNotNull(updatedAttribute);
        assertEquals("updated", updatedAttribute.getAttributeName());

        verify(attributeRepository, times(1)).findById(attributeId);
        verify(attributeRepository, times(1)).save(any(Attribute.class));
    }


    @Test
    public void testUpdateAttribute_EntityNotFoundException() {
        AttributeDto attributeDto = new AttributeDto("UpdatedName", true, "UpdatedDescription", "UpdatedUnit", false);

        when(attributeRepository.findById(attributeId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> attributeService.updateAttribute(attributeId, attributeDto));


        verify(attributeRepository, times(1)).findById(attributeId);
        verify(attributeRepository, never()).save(any(Attribute.class));
    }

    @Test
    public void testGetAllAttributeOptionInCompatibleEditFormat_shouldGetAllAttributeOptions() {
        List<AttributeOption> attributeOptionList = List.of(attributeOption);

        when(attributeOptionRepository.findAll()).thenReturn(attributeOptionList);

        CompatibleOptionGetResponse result = attributeService.getAllAttributeOptionsEditable();
        List<CompatibleOptionResponseDto> resultConfig = result.config();

        assertNotNull(result);
        assertEquals(null, result.id());
        assertEquals(1, resultConfig.size());
        assertEquals(attribute.getAttributeName(), resultConfig.get(0).type());

        verify(attributeOptionRepository, times(1)).findAll();
    }


}