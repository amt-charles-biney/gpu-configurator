package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.product.*;
import com.amalitech.gpuconfigurator.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final SearchService searchService;


    @CrossOrigin
    @GetMapping("/v1/product")
    public ResponseEntity<PageResponseDto> getAllProductUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String query
    ) {
        if (query != null && !query.isBlank()) {
            String[] brands = null;
            String[] priceRanges = null;
            if (brand != null && !brand.isBlank()) {
                brands = brand.strip().split(",");
            }
            if (price != null && !price.isBlank()) {
                priceRanges = price.strip().split(",");
            }
            return ResponseEntity.ok(
                    searchService.findProducts(
                            query,
                            page,
                            size == null ? 10 : size,
                            sort == null ? "createdAt" : sort,
                            brands,
                            priceRanges
                    )
            );
        }

        return null;
    }


}