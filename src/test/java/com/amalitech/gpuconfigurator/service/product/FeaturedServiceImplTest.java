package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeDto;
import com.amalitech.gpuconfigurator.dto.cases.CreateCaseRequest;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeaturedServiceImplTest {

    @InjectMocks
    private FeaturedServiceImpl featuredService;

    @Mock
    private ProductRepository productRepository;

    private AttributeDto attributeDto;
    private Attribute attribute;
    private UUID attributeId;

    private AttributeOption attributeOption;

    private CompatibleOption compatibleOption;
    private CategoryConfig categoryConfig;
    private Category category;
    private CompatibleOptionResponseDto compatibleOptionResponseDto;

    private Case productCase;

    private CreateCaseRequest createCaseRequestDto;

    private MultipartFile coverImage;

    private List<MultipartFile> images;

    private AttributeOption variant;

    private String coverImageUrl;

    private List<String> imageUrls;

    private Product product;

    private UUID productId;


    @BeforeEach
    void setUp() {
        attributeOption = AttributeOption.builder()
                .optionName("SampleOption")
                .priceAdjustment(BigDecimal.valueOf(25.0))
                .attribute(Attribute.builder()
                        .unit("SampleUnit")
                        .isMeasured(true)
                        .description("")
                        .attributeName("DISK")
                        .id(UUID.randomUUID())
                        .build())
                .id(UUID.randomUUID())
                .baseAmount(10.0F)
                .maxAmount(50.0F)
                .priceFactor(1.2)
                .build();


        compatibleOption = CompatibleOption.builder()
                .id(UUID.randomUUID())
                .categoryConfig(categoryConfig)
                .attributeOption(attributeOption)
                .isMeasured(true)
                .isCompatible(true)
                .isIncluded(true)
                .size(25)
                .createdAt(LocalDateTime.now())
                .build();

        categoryConfig = CategoryConfig.builder()
                .id(UUID.randomUUID())
                .category(category)
                .compatibleOptions(new ArrayList<>())
                .build();


        category = Category.builder()
                .id(UUID.randomUUID())
                .categoryName("TestCategory")
                .categoryConfig(categoryConfig)
                .build();


        String caseName = "Test Case";
        String caseDescription = "Description for test case";
        BigDecimal casePrice = new BigDecimal("10.99");
        List<UUID> incompatibleVariantIds = new ArrayList<>(List.of(UUID.fromString("2d187a4b-4013-4337-87ec-90531a2fb8b8")));
        coverImageUrl = "https://media.url/freqwfads";
        imageUrls = List.of("https://media.url/rqewirorewf", "https://media.url/rqewirorewf", "https://media.url/rqewirorewf");

        createCaseRequestDto = CreateCaseRequest.builder()
                .name(caseName)
                .description(caseDescription)
                .price(casePrice)
                .incompatibleVariants(incompatibleVariantIds)
                .build();

        coverImage = new MockMultipartFile("coverImage", new byte[0]);

        images = new ArrayList<>();
        images.add(new MockMultipartFile("image1", new byte[0]));
        images.add(new MockMultipartFile("image2", new byte[0]));
        images.add(new MockMultipartFile("image3", new byte[0]));

        variant = AttributeOption.builder()
                .id(incompatibleVariantIds.get(0))
                .build();

        productCase = Case.builder()
                .id(UUID.fromString("3d5bbd42-7431-48d4-90f7-447378aecb5c"))
                .name(caseName)
                .description(caseDescription)
                .coverImageUrl(coverImageUrl)
                .imageUrls(imageUrls)
                .price(casePrice)
                .incompatibleVariants(List.of(variant))
                .build();

        product = Product.builder()
                .productName("Test Product")
                .featured(false)
                .productAvailability(true)
                .serviceCharge(20.0)
                .inStock(10)
                .productCase(productCase)
                .category(category)
                .build();

//        productRepository.save(product);

        productId = UUID.randomUUID();

    }

    @Test
    void getAllFeaturedProduct() {
        //given
        List<Product> actualResponse = List.of(product);
        actualResponse.get(0).setFeatured(true);
        //when
        when(productRepository.getFeaturedProduct()).thenReturn(Optional.of(actualResponse));
        var expectedResponse = featuredService.getAllFeaturedProduct();
        //then
        assertNotNull(expectedResponse);

    }

    @Test
    void givenProduct_whenProductHasNoFeatured_thenReturnEmptyList() {
        List<Product> actualResponse = List.of(product);
        //when
        when(productRepository.getFeaturedProduct()).thenReturn(Optional.empty());
        var expectedResponse = featuredService.getAllFeaturedProduct();
        //then
        assertNotNull(expectedResponse);
    }


    @Test
    void addFeaturedProduct() {
        //given
        var actualProduct = product;
        actualProduct.setId(productId);
        //when
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        var expectedResponse = featuredService.addFeaturedProduct(actualProduct.getId());

        //then
        assertNotNull(expectedResponse);
        assertTrue(product.getFeatured());
        assertEquals("Now a featured Product", expectedResponse.message());

    }

    @Test
    void removeFeaturedProduct() {
        //given
        var actualProduct = product;
        actualProduct.setId(productId);

        //when
        when(productRepository.findById(actualProduct.getId())).thenReturn(Optional.of(product));
        var expectedResponse = featuredService.removeFeaturedProduct(productId);

        //then
        assertNotNull(expectedResponse);
        assertFalse(product.getFeatured());
        assertEquals("Product is not featured", expectedResponse.message());
    }

    @Test
    void givenProduct_whenProductNotExist_thenThrowsException() {
        //given
        var actualProduct = product;
        actualProduct.setId(productId);

        //when
        when(productRepository.findById(actualProduct.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            featuredService.removeFeaturedProduct(productId);

        });
        //then
        verify(productRepository, never()).save(any(Product.class));

    }
}