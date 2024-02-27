package com.amalitech.gpuconfigurator.service.product;


import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.product.*;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.repository.CaseRepository;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;
    private final CategoryRepository categoryRepository;
    private final CaseRepository caseRepository;
    private final CategoryConfigServiceImpl categoryConfig;


    @Override

    public CreateProductResponseDto createProduct(ProductDto request) {
        Category category = categoryService.getCategory(request.getCategory());


        Case productCase = caseRepository.findById(UUID.fromString(request.getProductCaseId())).orElseThrow(() -> new EntityNotFoundException("No case found"));

        BigDecimal totalConfigPrice = category.getCategoryConfig().getCompatibleOptions()
                .stream()
                .filter(CompatibleOption::getIsIncluded)
                .filter(configs -> !productCase.getIncompatibleVariants().contains(configs.getAttributeOption()))
                .map(configs -> configs.getAttributeOption().getPriceAdjustment())
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);


        var inStock = categoryConfig.getCategoryConfigByCategory(String.valueOf(category.getId())).inStock();

        BigDecimal percentageOfServiceChargeMultiplyByCasePrice = BigDecimal.valueOf(request.getServiceCharge())
                .divide(BigDecimal.valueOf(100))
                .multiply(productCase.getPrice().add(totalConfigPrice)).setScale(2, RoundingMode.HALF_UP);


        BigDecimal totalProductPrice = percentageOfServiceChargeMultiplyByCasePrice.add(totalConfigPrice.add(productCase.getPrice())).setScale(2, RoundingMode.HALF_UP);


        var product = Product
                .builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productCase(productCase)
                .serviceCharge(request.getServiceCharge())
                .totalProductPrice(totalProductPrice)
                .baseConfigPrice(totalConfigPrice)
                .category(category)
                .inStock(inStock)
                .productId(request.getProductId())
                .build();

        productRepository.save(product);

        return CreateProductResponseDto
                .builder()
                .id(product.getId().toString())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productPrice(product.getTotalProductPrice())
                .productCasePrice(product.getProductCase().getPrice())
                .baseConfigPrice(product.getBaseConfigPrice())
                .productId(product.getProductId())
                .productCase(product.getProductCase().getName())
                .productAvailability(product.getProductAvailability())
                .productCategory(category.getCategoryName())
                .coverImage(product.getProductCase().getCoverImageUrl())
                .imageUrl(product.getProductCase().getImageUrls())
                .inStock(product.getInStock())
                .createdAt(product.getCreatedAt())
                .build();
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .filter(product -> "!unassigned".equals(product.getCategory().getCategoryName()))
                .map(product -> ProductResponse.builder()
                        .productName(product.getProductName())
                        .id(product.getId().toString())
                        .productId(product.getProductId())
                        .productDescription(product.getProductDescription())
                        .productPrice(product.getTotalProductPrice())
                        .productBrand(product.getProductCase().getName())
                        .coverImage(product.getProductCase().getCoverImageUrl())
                        .isFeatured(product.getFeatured())
                        .category(ProductResponseDto.builder()
                                .name(product.getCategory().getCategoryName())
                                .id(String.valueOf(product.getCategory().getId()))
                                .build())
                        .imageUrl(product.getProductCase().getImageUrls())
                        .inStock(product.getInStock())
                        .build()).toList();
    }

    @Override
    public ProductResponseWithBrandDto getProduct(String id) {
        Product product = productRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException("product not found"));
        Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new NotFoundException("category does not exist "));

        return ProductResponseWithBrandDto.builder()
                .productName(product.getProductName())
                .id(product.getId().toString())
                .productId(product.getProductId())
                .productDescription(product.getProductDescription())
                .productPrice(product.getTotalProductPrice())
                .productAvailability(product.getProductAvailability())
                .productBrand(ProdcutBrandDto.builder()
                        .name(product.getProductCase().getName())
                        .price(product.getProductCase().getPrice())
                        .build())
                .coverImage(product.getProductCase().getCoverImageUrl())
                .inStock(product.getInStock())
                .isFeatured(product.getFeatured())
                .serviceCharge(product.getServiceCharge())
                .imageUrl(product.getProductCase().getImageUrls())
                .category(new ProductResponseDto(category.getCategoryName(), category.getId().toString()))
                .build();
    }


    public Page<ProductResponse> getAllProducts(int page, int size, String sort) {
        if (sort == null) {
            sort = "createdAt";
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Product> productPage = productRepository.findAll(pageRequest);

        List<ProductResponse> productResponseList = productPage.getContent().stream()
                .filter(product -> !"unassigned".equals(product.getCategory().getCategoryName()) && !product.getInStock().equals(0))
                .map(getProductProductResponseFunction())
                .toList();

        return new PageImpl<>(productResponseList, pageRequest, productPage.getTotalElements());
    }

    public Page<ProductResponse> getAllProductsAdmin(int page, int size, String sort) {
        if (sort == null) {
            sort = "createdAt";
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Product> productPage = productRepository.findAll(pageRequest);

        return productPage
                .map(getProductProductResponseFunction());

    }

    @NotNull
    private static Function<Product, ProductResponse> getProductProductResponseFunction() {
        return product -> {
            String stockLowOrMore = product.getInStock() <= 5 ? "Low Stock" : "Available";
            String stockStatus = product.getInStock() == 0 ? "No Stock" : stockLowOrMore;
            return ProductResponse.builder()
                    .productName(product.getProductName())
                    .id(product.getId().toString())
                    .productId(product.getProductId())
                    .productBrand(product.getProductCase().getName())
                    .productDescription(product.getProductDescription())
                    .productPrice(product.getTotalProductPrice())
                    .coverImage(product.getProductCase().getCoverImageUrl())
                    .isFeatured(product.getFeatured())
                    .productBrand(product.getProductCase().getName())
                    .category(ProductResponseDto.builder()
                            .name(product.getCategory().getCategoryName())
                            .id(String.valueOf(product.getCategory().getId()))
                            .build())
                    .stockStatus(stockStatus)
                    .imageUrl(product.getProductCase().getImageUrls())
                    .inStock(product.getInStock())
                    .build();
        };
    }


    @Override
    public ProductResponse updateProduct(UUID id, ProductUpdateDto updatedProductDto) {
        try {
            Product existingProduct = productRepository.findById(id).orElseThrow(() -> new NotFoundException("No product with this id" + " " + id));

            if (updatedProductDto.getProductName() != null) {
                existingProduct.setProductName(updatedProductDto.getProductName());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getProductDescription() != null) {
                existingProduct.setProductDescription(updatedProductDto.getProductDescription());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getServiceCharge() != null) {
                existingProduct.setServiceCharge(updatedProductDto.getServiceCharge());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getProductId() != null) {
                existingProduct.setProductId(updatedProductDto.getProductId());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getProductCaseId() != null) {
                Case newCase = caseRepository.findById(UUID.fromString(updatedProductDto.getProductCaseId())).orElseThrow(() -> new EntityNotFoundException("No case found"));
                existingProduct.setProductCase(newCase);
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getCategory() != null) {
                Category category = categoryService.getCategory(updatedProductDto.getCategory());

                var inStock = categoryConfig.getCategoryConfigByCategory(String.valueOf(category.getId())).inStock();

                existingProduct.setCategory(category);
                existingProduct.setInStock(inStock);

                existingProduct.setUpdatedAt(LocalDateTime.now());
            }

            Product updatedProduct = productRepository.save(existingProduct);
            return mapProductToProductResponse(updatedProduct);
        } catch (NotFoundException e) {
            throw new NotFoundException("No product found");
        }
    }

    @Override
    public GenericResponse deleteBulkProducts(List<String> productIds) {
        List<UUID> selectedProductUUID = productIds.stream()
                .map(UUID::fromString)
                .toList();

        productRepository.deleteAllById(selectedProductUUID);
        return new GenericResponse(HttpStatus.ACCEPTED.value(), "deleted bulk products successful");
    }

    @Override
    public void updateCategoryStock(UUID categoryId, Integer stock) {
        List<Product> products = productRepository.findProductsByCategoryName(categoryId);

        for (var product : products) {
            product.setInStock(stock);
        }

    }

    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }

    public List<FeaturedProductDto> getNewProducts() {
        LocalDateTime timeRequest = LocalDateTime.now().minusHours(24);
        var products = productRepository.getBrandNewProducts(timeRequest).orElse(Collections.emptyList());
        return products.stream().map(product -> FeaturedProductDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .productBrand(product.getProductCase().getName())
                .productPrice(product.getTotalProductPrice())
                .coverImage(product.getProductCase().getCoverImageUrl())
                .build()).toList();
    }

    private ProductResponse mapProductToProductResponse(Product updatedProduct) {
        return ProductResponse.builder()
                .id(String.valueOf(updatedProduct.getId()))
                .productName(updatedProduct.getProductName())
                .productId(updatedProduct.getProductId())
                .productDescription(updatedProduct.getProductDescription())
                .productPrice(updatedProduct.getTotalProductPrice())
                .inStock(updatedProduct.getInStock())
                .serviceCharge(updatedProduct.getServiceCharge())
                .productAvailability(updatedProduct.getProductAvailability())
                .build();
    }
}
