package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.ProductDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;
    private final CategoryRepository categoryRepository;
    private final UploadImageServiceImpl cloudianryImage;


    public CreateProductResponseDto createProduct(ProductDto request, List<MultipartFile> files) {
        Category category = categoryService.getCategory(request.getCategory());

        List<String> imageUrls = files.stream()
                .map(this.cloudianryImage::upload)
                .toList();

        var product = Product
                .builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productPrice(request.getProductPrice())
                .category(category)
                .imageUrl(imageUrls)
                .productId(request.getProductId())
                .build();

        productRepository.save(product);

        return CreateProductResponseDto
                .builder()
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productId(product.getProductId())
                .productAvailability(product.getProductAvailability())
                .productCategory(category.getCategoryName())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .build();
    }


    public Page<Product> getAllProducts(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("productPrice").ascending());
        return productRepository.findAll(pageRequest);
    }

    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }



}
