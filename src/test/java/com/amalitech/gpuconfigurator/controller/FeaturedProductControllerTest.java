package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.service.product.FeaturedService;
import com.amalitech.gpuconfigurator.service.product.FeaturedServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FeaturedProductControllerTest {

    @InjectMocks
    private FeaturedProductController featuredProductController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeaturedServiceImpl featuredService;


    @Test
    void getAllFeaturedProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/brand"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void addFeaturedProduct() {
    }

    @Test
    void removeFeaturedProduct() {
    }
}