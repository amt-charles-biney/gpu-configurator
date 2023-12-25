package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.ProductDto;
import com.amalitech.gpuconfigurator.model.Otp;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.service.UploadImageService;
import com.amalitech.gpuconfigurator.service.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    private final UploadImageService cloudinaryImage;


    @PostMapping("/v1/admin/product")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateProductResponseDto addProduct(@Valid @ModelAttribute ProductDto request,
                                               @RequestParam("file") List<MultipartFile> files) {
        return productService.createProduct(request, files);
    }


    @GetMapping("/v1/admin/product")
    public List<Product> getAllProducts(){
        return productService.getAllProduct();
    }


    @GetMapping("/v1/admin/product/{productId}")
    public Product getProductByProductId(@PathVariable("productId") String productId){
        return productService.getProductByProductId(productId);
    }



    @DeleteMapping("/v1/admin/product/{id}")
    public void deleteProduct(@PathVariable("id")UUID id) {
        productService.deleteProductById(id);
    }


}
