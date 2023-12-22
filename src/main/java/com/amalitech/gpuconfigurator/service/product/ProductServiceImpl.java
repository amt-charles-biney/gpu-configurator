package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.product.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.product.ProductDto;
import com.amalitech.gpuconfigurator.dto.product.ProductUpdateDto;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.model.product.Product;
import com.amalitech.gpuconfigurator.repository.category.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.product.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;
    private final CategoryRepository categoryRepository;

    public CreateProductResponseDto createProduct(ProductDto request) {
        Category category = categoryService.getCategory(request.getCategory());

        var product = Product
                .builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productPrice(request.getProductPrice())
                .category(category)
                .productId(request.getProductId())
                .build();

        productRepository.save(product);


        return CreateProductResponseDto
                .builder()
                .productName(product.getProductName())
                .id(product.getId().toString())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productId(product.getProductId())
                .productAvailability(product.getProductAvailability())
                .productCategory(category.getCategoryName())
                .createdAt(product.getCreatedAt())
                .build();
    }


    public List<CreateProductResponseDto> getAllProduct() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(option -> CreateProductResponseDto.
                        builder()
                        .productPrice(option.getProductPrice())
                        .productAvailability(option.getProductAvailability())
                        .id(option.getId().toString())
                        .productId(option.getProductId())
                        .productName(option.getProductName())
                        .productDescription(option.getProductDescription())
                        .build())
                .toList();
    }


    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public CreateProductResponseDto getProductByProductId(String productId) {
        Product option = productRepository.getProductByProductId(productId).orElseThrow(() -> new EntityNotFoundException("product does not exist"));
        return CreateProductResponseDto.
                builder()
                .productPrice(option.getProductPrice())
                .productAvailability(option.getProductAvailability())
                .id(option.getId().toString())
                .productId(option.getProductId())
                .productName(option.getProductName())
                .productDescription(option.getProductDescription())
                .build();
    }

    @Override
    public GenericResponse updateProduct(UUID productId, ProductUpdateDto updatedProduct) {

        Product optionalProduct = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("could not update product"));

        optionalProduct.setProductAvailability(updatedProduct.availability());
        optionalProduct.setProductDescription(updatedProduct.productDescription());
        optionalProduct.setProductName(updatedProduct.productName());
        optionalProduct.setProductPrice(updatedProduct.productPrice());
        optionalProduct.setUpdated_at(LocalDateTime.now());
        optionalProduct.setProductId(updatedProduct.productId());

        productRepository.save(optionalProduct);

        return new GenericResponse(201, "updated successfully ");
    }

}
