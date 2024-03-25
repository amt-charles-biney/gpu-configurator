package com.amalitech.gpuconfigurator.service.product;


import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.VariantStockLeastDto;
import com.amalitech.gpuconfigurator.dto.product.*;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.*;
import com.amalitech.gpuconfigurator.repository.CaseRepository;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.attribute.AttributeService;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import com.amalitech.gpuconfigurator.service.categoryConfig.CategoryConfigServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
import java.util.ArrayList;
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
    private final AttributeService attributeService;


    @Override

    public CreateProductResponseDto createProduct(ProductDto request) {
        Category category = categoryService.getCategory(request.getCategory());
        Case productCase = caseRepository.findById(UUID.fromString(request.getProductCaseId())).orElseThrow(() -> new EntityNotFoundException("No case found"));

        BigDecimal totalConfigPrice = getTotalConfigIncludedPrice(category, productCase);
        BigDecimal serviceChargePercentage = getPercentageOfServiceChargeMultiplyByCasePrice(request.getServiceCharge(), productCase, totalConfigPrice);
        BigDecimal totalProductPrice = getProductTotalPrice(serviceChargePercentage, totalConfigPrice, productCase);

        Product product = Product
                .builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productCase(productCase)
                .serviceCharge(request.getServiceCharge())
                .totalProductPrice(totalProductPrice)
                .baseConfigPrice(totalConfigPrice)
                .category(category)
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
                        .serviceCharge(product.getServiceCharge())
                        .build()).toList();
    }

    public Product getProductOnDemand(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("could not find product"));

        Case productCase = product.getProductCase();
        BigDecimal totalConfigPrice = getTotalConfigIncludedPrice(product.getCategory(), productCase);
        BigDecimal serviceChargePercentage = getPercentageOfServiceChargeMultiplyByCasePrice(product.getServiceCharge(), productCase, totalConfigPrice);
        BigDecimal totalProductPrice = getProductTotalPrice(serviceChargePercentage, totalConfigPrice, productCase);

        product.setTotalProductPrice(totalProductPrice);
        product.setBaseConfigPrice(totalConfigPrice);

        return product;
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
                .filter(prod -> {
                    var inStock = categoryConfig.getCategoryConfigByCategory(String.valueOf(prod.getCategory().getId())).inStock();

                    return inStock != 0 && !"unassigned".equals(prod.getCategory().getCategoryName());
                })
                .map(getProductProductResponseFunction())
                .toList();

        return new PageImpl<>(productResponseList, pageRequest, productResponseList.size());
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
    private Function<Product, ProductResponse> getProductProductResponseFunction() {
        return product -> {
            String stockStatus = "";

            List<AttributeResponse> attributeOptions = Collections.emptyList();

            Integer inStock = 0;
            try {
                List<VariantStockLeastDto> totalLeastStock = categoryConfig.getCategoryConfigByCategory(String.valueOf(product.getCategory().getId())).totalLeastStocks();

                inStock = categoryConfig.getCategoryConfigByCategory(String.valueOf(product.getCategory().getId())).inStock();

                String stockLowOrMore = inStock <= 5 ? "Low Stock" : "Available";
                stockStatus = inStock == 0 ? "No Stock" : stockLowOrMore;

                List<String> attributeResponses = totalLeastStock.stream()
                        .map(VariantStockLeastDto::attributeResponse)
                        .toList();

                attributeOptions = attributeResponses.stream()
                        .map(x -> attributeService.getAttributeById(UUID.fromString(x)))
                        .toList();
            } catch (EntityNotFoundException ex) {
                // handling the exception here will cause the UI to break
            }

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
                    .serviceCharge(product.getServiceCharge())
                    .imageUrl(product.getProductCase().getImageUrls())
                    .inStock(inStock)
                    .totalLeastStock(attributeOptions)
                    .build();
        };
    }


    @Override
    @Transactional
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

                BigDecimal updatedPercentageOfServiceChargeMultiplyByCasePrice = BigDecimal.valueOf(updatedProductDto.getServiceCharge())
                        .divide(BigDecimal.valueOf(100))
                        .multiply(existingProduct.getProductCase().getPrice().add(existingProduct.getBaseConfigPrice())).setScale(2, RoundingMode.HALF_UP);

                BigDecimal updatedTotalPrice = updatedPercentageOfServiceChargeMultiplyByCasePrice
                        .add(existingProduct.getBaseConfigPrice()).add(existingProduct.getProductCase().getPrice())
                        .setScale(2, RoundingMode.HALF_UP);

                existingProduct.setTotalProductPrice(updatedTotalPrice);
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

                existingProduct.setCategory(category);
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
    @Transactional
    public void updateCategoryStock(UUID categoryId, UpdateProductConfigsDto request) {
        updateProductPrices(productRepository.findProductsByCategoryName(categoryId), request.getBaseConfigPrice());
    }

    @Override
    @Transactional
    public void updateTotalPriceWhenUpdatingCase(UUID caseId, BigDecimal casePrice) {
        updateProductPrices(productRepository.findProductsByProductCase(caseId), casePrice);
    }

    private void updateProductPrices(List<Product> products, BigDecimal baseConfigPrice) {
        for (var product : products) {
            Double serviceCharge = product.getServiceCharge();

            BigDecimal updatedPercentageOfServiceChargeMultiplyByCasePrice = BigDecimal.valueOf(serviceCharge)
                    .divide(BigDecimal.valueOf(100))
                    .multiply(product.getProductCase().getPrice().add(baseConfigPrice)).setScale(2, RoundingMode.HALF_UP);

            BigDecimal updatedTotalPrice = updatedPercentageOfServiceChargeMultiplyByCasePrice
                    .add(baseConfigPrice).add(product.getProductCase().getPrice())
                    .setScale(2, RoundingMode.HALF_UP);

            product.setTotalProductPrice(updatedTotalPrice);
            productRepository.save(product);
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
                .serviceCharge(updatedProduct.getServiceCharge())
                .productAvailability(updatedProduct.getProductAvailability())
                .build();
    }


    private BigDecimal getTotalConfigIncludedPrice(Category category, Case productCase) {
        return category.getCategoryConfig().getCompatibleOptions()
                .stream()
                .filter(CompatibleOption::getIsIncluded)
                .filter(configs -> !productCase.getIncompatibleVariants().contains(configs.getAttributeOption()))
                .map(configs -> configs.getAttributeOption().getPriceAdjustment())
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getPercentageOfServiceChargeMultiplyByCasePrice(double serviceCharge, Case productCase, BigDecimal totalConfigPrice) {
        return BigDecimal.valueOf(serviceCharge)
            .divide(BigDecimal.valueOf(100))
            .multiply(productCase.getPrice().add(totalConfigPrice)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getProductTotalPrice(BigDecimal percentageOfServiceChargeXCasePrice, BigDecimal totalConfigPrice, Case productCase) {
        return percentageOfServiceChargeXCasePrice.add(totalConfigPrice.add(productCase.getPrice())).setScale(2, RoundingMode.HALF_UP);
    }
}
