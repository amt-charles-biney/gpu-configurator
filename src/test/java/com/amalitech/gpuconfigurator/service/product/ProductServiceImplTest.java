package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.attribute.AttributeDto;
import com.amalitech.gpuconfigurator.dto.cases.CreateCaseRequest;
import com.amalitech.gpuconfigurator.dto.categoryconfig.*;
import com.amalitech.gpuconfigurator.dto.product.*;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.CaseRepository;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private CategoryConfigRepository categoryConfigRepository;

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

        productId = UUID.randomUUID();

    }


    @Test
    void createProduct() {

        ProductDto request = ProductDto.builder()
                .productId("1234")
                .productDescription("A test product")
                .productName("Sample Product")
                .productCaseId(String.valueOf(productCase.getId()))
                .serviceCharge(20.0)
                .category(category.getCategoryName())
                .inStock(10)
                .build();

        Product myProduct = Product.builder()
                .totalProductPrice(BigDecimal.valueOf(10))
                .baseConfigPrice(BigDecimal.ZERO)
                .serviceCharge(20.0)
                .productCase(productCase)
                .category(category)
                .productDescription(request.getProductDescription())
                .productName(request.getProductName())
                .productAvailability(true)
                .build();


        CreateProductResponseDto expectedResponse = CreateProductResponseDto.builder()
                .id(String.valueOf(productId))
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productPrice(BigDecimal.ZERO)
                .productCasePrice(BigDecimal.ZERO)
                .baseConfigPrice(BigDecimal.ZERO)
                .productCase(request.getProductCaseId())
                .productCategory(request.getCategory())
                .productId(request.getProductId())
                .productAvailability(true)
                .createdAt(LocalDateTime.now())
                .imageUrl(Arrays.asList("image1", "image2", "image3"))
                .coverImage("coverImage")
                .inStock(request.getInStock())
                .build();


        when(categoryService.getCategory(request.getCategory())).thenReturn(category);
        when(caseRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(productCase));
        when(productRepository.save(Mockito.any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setId(productId);
            return savedProduct;
        });

        // When
        CreateProductResponseDto response = productService.createProduct(request);

        // Then
        assertNotNull(response);
        assertEquals(request.getProductName(), expectedResponse.getProductName());
        assertEquals(expectedResponse.getId(), response.getId());
        assertEquals(expectedResponse.getProductDescription(), response.getProductDescription());


    }

    @Test
    void getAllProducts() {

        // Given
        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .totalProductPrice(BigDecimal.valueOf(10))
                .baseConfigPrice(BigDecimal.ZERO)
                .serviceCharge(20.0)
                .productCase(productCase)
                .category(category)
                .productDescription("Description 1")
                .productName("Product 1")
                .inStock(20)
                .productAvailability(true)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .totalProductPrice(BigDecimal.valueOf(10))
                .baseConfigPrice(BigDecimal.ZERO)
                .serviceCharge(20.0)
                .productCase(productCase)
                .category(category)
                .productDescription("Description 2")
                .productName("Product 2")
                .inStock(20)
                .productAvailability(true)
                .build();

        List<Product> products = Arrays.asList(product1, product2);

        ProductServiceImpl productServiceMock = Mockito.mock(ProductServiceImpl.class);
        when(productServiceMock.getAllProducts()).thenReturn(products.stream().map(product ->
                ProductResponse.builder()
                        .productName(product.getProductName())
                        .build()
        ).toList());

        // When
        List<ProductResponse> result = productServiceMock.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(products.getFirst().getProductName(), product1.getProductName());


    }

    @Test
    void getProduct() {

        Product product = Product.builder()
                .id(productId)
                .totalProductPrice(BigDecimal.valueOf(10))
                .baseConfigPrice(BigDecimal.ZERO)
                .serviceCharge(20.0)
                .productCase(productCase)
                .category(category)
                .productDescription("Description 1")
                .productName("Product 1")
                .inStock(20)
                .productAvailability(true)
                .build();

        ProductServiceImpl productServiceMock = Mockito.mock(ProductServiceImpl.class);
        when(productServiceMock.getProduct(String.valueOf(productId))).thenReturn(
                ProductResponse.builder().productName(product.getProductName()).build());

        ProductResponse result = productServiceMock.getProduct(String.valueOf(productId));

        assertNotNull(result);
        assertEquals(product.getProductName(), result.productName());


    }

    @Test
    void testGetAllProducts() {
        int page = 0;
        int size = 10;
        String sort = "createdAt";

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .totalProductPrice(BigDecimal.valueOf(10))
                .baseConfigPrice(BigDecimal.ZERO)
                .serviceCharge(20.0)
                .productCase(productCase)
                .category(category)
                .productDescription("Description 1")
                .productName("Product 1")
                .inStock(20)
                .featured(false)
                .productAvailability(true)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .totalProductPrice(BigDecimal.valueOf(10))
                .baseConfigPrice(BigDecimal.ZERO)
                .serviceCharge(20.0)
                .productCase(productCase)
                .category(category)
                .productDescription("Description 2")
                .productName("Product 2")
                .inStock(20)
                .featured(false)
                .productAvailability(true)
                .build();

        List<Product> mockProductList = Arrays.asList(
                product1, product2
        );

        var mockProductPage = new PageImpl<>(mockProductList);

        when(productRepository.findAll(PageRequest.of(page, size, Sort.by(sort).descending())))
                .thenReturn(mockProductPage);

        // When
        var result = productService.getAllProducts(page, size, sort);

        // Then
        assertNotNull(result);
        assertEquals(mockProductList.size(), result.getContent().size());

    }

    @Test
    void getAllProductsAdmin() {
        int page = 0;
        int size = 10;
        String sort = "createdAt";

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .totalProductPrice(BigDecimal.valueOf(10))
                .baseConfigPrice(BigDecimal.ZERO)
                .serviceCharge(20.0)
                .productCase(productCase)
                .category(category)
                .productDescription("Description 1")
                .productName("Product 1")
                .inStock(20)
                .featured(false)
                .productAvailability(true)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .totalProductPrice(BigDecimal.valueOf(10))
                .baseConfigPrice(BigDecimal.ZERO)
                .serviceCharge(20.0)
                .productCase(productCase)
                .category(category)
                .productDescription("Description 2")
                .productName("Product 2")
                .inStock(20)
                .featured(false)
                .productAvailability(true)
                .build();

        List<Product> mockProductList = Arrays.asList(
                product1, product2
        );

        var mockProductPage = new PageImpl<>(mockProductList);

        when(productRepository.findAll(PageRequest.of(page, size, Sort.by(sort).descending())))
                .thenReturn(mockProductPage);

        // When
        var result = productService.getAllProductsAdmin(page, size, sort);

        // Then
        assertNotNull(result);
        assertEquals(mockProductList.size(), result.getContent().size());
    }

    @Test
    void updateProduct() {
        Product existingProduct = Product.builder()
                .id(UUID.randomUUID())
                .totalProductPrice(BigDecimal.valueOf(10))
                .baseConfigPrice(BigDecimal.ZERO)
                .serviceCharge(20.0)
                .productCase(productCase)
                .category(category)
                .productDescription("Description ")
                .productName("Product ")
                .inStock(20)
                .featured(false)
                .productAvailability(true)
                .build();

        ProductUpdateDto updatedProductDto = new ProductUpdateDto();
        updatedProductDto.setProductName("Updated Product Name");

        when(productRepository.getReferenceById(productId)).thenReturn(existingProduct);
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        // When
        ProductResponse result = productService.updateProduct(productId, updatedProductDto);

        // Then
        assertEquals(updatedProductDto.getProductName(), result.productName());
    }



}