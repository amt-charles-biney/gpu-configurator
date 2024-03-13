package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeDto;
import com.amalitech.gpuconfigurator.dto.cases.CreateCaseRequest;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilteringServiceImplTest {

    @InjectMocks
    private FilteringServiceImpl filteringService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ConfigOptionsFiltering configOptionsFiltering;
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

//        productId = UUID.randomUUID();

    }

    @Test
    @DisplayName("When no parameters are provided")
    void filterProduct() {
        // Given

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .productName("prod 1")
                .category(category)
                .productDescription("prod 1 test")
                .productAvailability(true)
                .totalProductPrice(BigDecimal.valueOf(1000))
                .productCase(productCase)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .productName("prod 2")
                .category(category)
                .productDescription("prod 2 test")
                .productAvailability(true)
                .totalProductPrice(BigDecimal.valueOf(100))
                .productCase(productCase)
                .build();

        List<Product> actualResponse = List.of(product1, product2);

        //when
        when(filteringService.filterProduct(null, null, null, null, null, null)).thenReturn(actualResponse);
        var expectedResponse = filteringService.filterProduct(null, null, null, null, null, null);

        //then
        assertNotNull(expectedResponse);

    }

    @Test
    @DisplayName("When only product Case parameters are provided")
    void GiveCaseName_whenOnlyCaseProvided_thenOnlyMatchCaseName() {
        // Given
        String productCaseVar = "Test Case";


        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .productName("prod 1")
                .category(category)
                .productDescription("prod 1 test")
                .productAvailability(true)
                .totalProductPrice(BigDecimal.valueOf(1000))
                .productCase(productCase)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .productName("prod 2")
                .category(category)
                .productDescription("prod 2 test")
                .productAvailability(true)
                .totalProductPrice(BigDecimal.valueOf(100))
                .productCase(null)
                .build();

        List<Product> actualResponse = List.of(product1);

        //when
        when(productRepository.findAll(Mockito.any(Specification.class))).thenReturn(actualResponse);
        var expectedResponse = filteringService.filterProduct(productCaseVar, null, null, null, null, null);

        //then
        assertNotNull(expectedResponse);

    }

    @Test
    @DisplayName("When  product Case and Price range parameters are provided")
    void GiveCaseName_whenCaseAndPriceProvided_thenOnlyMatchCaseNameAndPrice() {
        // Given
        String productCaseVar = "Test Case";
        String price = "1000-2000";


        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .productName("prod 1")
                .category(category)
                .productDescription("prod 1 test")
                .productAvailability(true)
                .totalProductPrice(BigDecimal.valueOf(1000))
                .productCase(productCase)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .productName("prod 2")
                .category(category)
                .productDescription("prod 2 test")
                .productAvailability(true)
                .totalProductPrice(BigDecimal.valueOf(100))
                .productCase(null)
                .build();

        List<Product> actualResponse = List.of(product1, product2);

        //when
        when(productRepository.findAll(Mockito.any(Specification.class))).thenReturn(actualResponse);
        var expectedResponse = filteringService.filterProduct(productCaseVar, price, null, null, category.getCategoryName(), null);

        //then
        assertNotNull(expectedResponse);
        assertEquals(1, expectedResponse.size());

    }
}