package com.amalitech.gpuconfigurator.service.product;


<<<<<<<HEAD
import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponseDto;
import com.amalitech.gpuconfigurator.dto.product.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.product.ProductDto;
import com.amalitech.gpuconfigurator.dto.product.ProductResponse;
import com.amalitech.gpuconfigurator.dto.product.ProductUpdateDto;
=======
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.product.*;
>>>>>>>cc924ca(feat:product deletion in bulk)
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import com.amalitech.gpuconfigurator.service.cloudinary.UploadImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;
    private final CategoryRepository categoryRepository;
    private final UploadImageServiceImpl cloudinaryImage;


    @Override

    public CreateProductResponseDto createProduct(ProductDto request, List<MultipartFile> files, MultipartFile coverImage) {
        Category category = categoryService.getCategory(request.getCategory());

        List<String> imageUrls = files.stream()
                .map(this.cloudinaryImage::upload)
                .toList();
        String coverImageUrl = cloudinaryImage.uploadCoverImage(coverImage);

        var product = Product
                .builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productPrice(request.getProductPrice())
                .category(category)
                .imageUrl(imageUrls)
                .coverImage(coverImageUrl)
                .inStock(request.getInStock())
                .productId(request.getProductId())
                .build();

        productRepository.save(product);

        return CreateProductResponseDto
                .builder()
                .id(product.getId().toString())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productId(product.getProductId())
                .productAvailability(product.getProductAvailability())
                .productCategory(category.getCategoryName())
                .coverImage(product.getCoverImage())
                .imageUrl(product.getImageUrl())
                .inStock(product.getInStock())
                .createdAt(product.getCreatedAt())
                .build();
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> ProductResponse.builder()
                        .productName(product.getProductName())
                        .id(product.getId().toString())
                        .productId(product.getProductId())
                        .productDescription(product.getProductDescription())
                        .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                        .coverImage(product.getCoverImage())
                        .imageUrl(product.getImageUrl())
                        .isFeatured(product.getFeatured())
                        .category(AttributeResponseDto.builder().name(product.getCategory().getCategoryName())
                                .id(String.valueOf(product.getCategory().getId()))
                                .build())
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
                .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                .productAvailability(product.getProductAvailability())
<<<<<<< HEAD
                .coverImage(product.getCoverImage())
=======
                .productBrand(ProdcutBrandDto.builder()
                        .name(product.getProductCase().getName())
                        .price(product.getProductCase().getPrice())
                        .build())
                .coverImage(product.getProductCase().getCoverImageUrl())
>>>>>>> b5a2ef0 (fix: get product response with brand name and price)
                .inStock(product.getInStock())
                .imageUrl(product.getImageUrl().stream().toList())
                .category(new AttributeResponseDto(category.getCategoryName(), category.getId().toString()))
                .build();
    }


    public Page<ProductResponse> getAllProducts(int page, int size, String sort) {
        if (sort == null) sort = "createdAt";
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<Product> productPage = productRepository.findAll(pageRequest);

        return productPage.map(product -> ProductResponse.builder()
                .productName(product.getProductName())
                .id(product.getId().toString())
                .productId(product.getProductId())
                .productDescription(product.getProductDescription())
                .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                .coverImage(product.getCoverImage())
                .imageUrl(product.getImageUrl())
                .isFeatured(product.getFeatured())
                .category(AttributeResponseDto.builder().name(product.getCategory().getCategoryName())
                        .id(String.valueOf(product.getCategory().getId()))
                        .build())
                .inStock(product.getInStock())
                .build());
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
                    .productCase(product.getProductCase().getName())
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
    public ProductResponse updateProduct(UUID id, ProductUpdateDto updatedProductDto, List<MultipartFile> files, MultipartFile coverImage) {
        try {
            Product existingProduct = productRepository.getReferenceById(id);

            if (updatedProductDto.getProductName() != null) {
                existingProduct.setProductName(updatedProductDto.getProductName());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getProductDescription() != null) {
                existingProduct.setProductDescription(updatedProductDto.getProductDescription());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getProductPrice() != null) {
                existingProduct.setProductPrice(updatedProductDto.getProductPrice());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getProductId() != null) {
                existingProduct.setProductId(updatedProductDto.getProductId());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getInStock() != null) {
                existingProduct.setInStock(updatedProductDto.getInStock());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getBrand() != null) {
                existingProduct.setProductBrand(updatedProductDto.getBrand());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getCategory() != null) {
                Category category = categoryService.getCategory(updatedProductDto.getCategory());

                var inStock = categoryConfig.getCategoryConfigByCategory(String.valueOf(category.getId())).inStock();
                System.out.println("------------------------");
                System.out.println(inStock);

                existingProduct.setCategory(category);
                existingProduct.setInStock(inStock);

                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (files != null) {
                List<String> imageUrls = files.stream()
                        .map(this.cloudinaryImage::upload)
                        .toList();
                existingProduct.setImageUrl(imageUrls);
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (coverImage != null) {
                String coverImageUrl = cloudinaryImage.uploadCoverImage(coverImage);
                existingProduct.setCoverImage(coverImageUrl);
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
                .productPrice(BigDecimal.valueOf(updatedProduct.getProductPrice()))
                .inStock(updatedProduct.getInStock())
                .productAvailability(updatedProduct.getProductAvailability())
                .build();
    }


    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }


}
