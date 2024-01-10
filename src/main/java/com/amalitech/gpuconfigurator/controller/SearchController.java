package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.search.ProductSearchResponseDto;
import com.amalitech.gpuconfigurator.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @CrossOrigin
    @GetMapping("/v1/search/products")
    public ResponseEntity<ProductSearchResponseDto> findProducts(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0", name = "page", required = false) Integer pageNo,
            @RequestParam(defaultValue = "10", name = "size", required = false) Integer pageSize,
            @RequestParam(defaultValue = "createdAt", name = "sort", required = false) String sortField,
            @RequestParam(required = false) String[] brand,
            @RequestParam(required = false) String[] price
    ) {
        return ResponseEntity.ok(
                searchService.findProducts(
                        query,
                        pageNo,
                        pageSize,
                        sortField,
                        brand,
                        price
                )
        );
    }
}