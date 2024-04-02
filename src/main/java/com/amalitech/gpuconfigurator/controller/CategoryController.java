package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.categoryconfig.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryRequestDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryResponse;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;
    @Operation(
            summary = "Create category",
            method = "POST"
    )
    @CrossOrigin
    @PostMapping("/v1/admin/category")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody CategoryRequestDto request) {
        return categoryService.createCategory(request);
    }

    @Operation(
            summary = "Get all categories (for admin)",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/category")
    @ResponseStatus(HttpStatus.OK)
    public List<AllCategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Operation(
            summary = "Get all categories for user",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/category")
    @ResponseStatus(HttpStatus.OK)
    public List<AllCategoryResponse> getAllCategoriesUser() {
        return categoryService.getAllCategories();
    }

    @Operation(
            summary = "Get category by name",
            method = "GET"
    )
    @CrossOrigin
    @GetMapping("/v1/admin/category/{name}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getCategoryByName(@PathVariable("name") String name) {
        return categoryService.getCategoryByName(name);
    }

}
