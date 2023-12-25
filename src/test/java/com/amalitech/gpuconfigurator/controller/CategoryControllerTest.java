package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.CategoryRequestDto;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Test
    @DisplayName("Test success for creating new category")
    void createCategory() {
        CategoryRequestDto dto = new CategoryRequestDto("GPU");
        Category data = Category.builder()
                .id(UUID.fromString("6c4a87df-6831-4d49-a924-42979ec657ba"))
                .categoryName(dto.name())
                .build();

        when(categoryController.createCategory(dto)).thenReturn(data);
        Category response =  categoryController.createCategory(dto);

        assertNotNull(response.getId());
        assertEquals(HttpStatus.CREATED.value(),201);

    }

    @Test
    @DisplayName("Test success for getting all category")
    void getAllCategoriesTest(){

        List<AllCategoryResponse> categoryList = new ArrayList<>();
        AllCategoryResponse dataOne = AllCategoryResponse
                .builder()
                .categoryName("SERVER")
                .build();
        AllCategoryResponse dataTwo = AllCategoryResponse
                .builder()
                .categoryName("SERVER")
                .build();

        categoryList.add(dataOne);
        categoryList.add(dataTwo);
        when(categoryController.getAllCategories()).thenReturn(categoryList);
       var response = categoryController.getAllCategories();
       assertEquals(2, response.size());
       assertEquals(HttpStatus.OK.value(),200);

    }

    @Test
    @DisplayName("Test success for getting all category")
    void getCategoryByNameTest(){

        List<Category> listOfCategory = new ArrayList<>();
        Category data = Category.builder()
                .id(UUID.fromString("6c4a87df-6831-4d49-a924-42979ec657ba"))
                .categoryName("GPU")
                .build();
        listOfCategory.add(data);
        when(categoryController.getCategoryByName("GPU")).thenReturn(listOfCategory);
       var response =  categoryController.getCategoryByName("GPU");
//       assertEquals(data,response.);
    }
}
