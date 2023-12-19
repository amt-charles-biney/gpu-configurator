package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.ProductDto;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.model.product.Product;
import com.amalitech.gpuconfigurator.repository.product.ProductRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;

    public CreateProductResponseDto createProduct(ProductDto request) {
        Category category = categoryService.getOrCreateCategory(request.getCategoryName());

        var product = Product
                .builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productPrice(request.getProductPrice())
                .category(category)
                .build();

        productRepository.save(product);

        return CreateProductResponseDto
                .builder()
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productAvailability(product.getProductAvailability())
                .productCategory(category.getCategoryName())
                .createdAt(product.getCreatedAt())
                .build();
    }

    public List<Product> getAllProduct() {

        var allProducts = productRepository.findAll();

       if(allProducts.isEmpty()){
           return Collections.emptyList();
       }
        return allProducts;
    }

    public List<Product> getProductByName(String name) {
        List<Product> products = productRepository.findProductsByName(name);
        if (products.isEmpty()){
            return Collections.emptyList();
        }
        return products;
    }

    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }

}
