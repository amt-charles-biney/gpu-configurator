package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.brand.BrandDto;
import com.amalitech.gpuconfigurator.dto.brand.BrandRequest;
import com.amalitech.gpuconfigurator.model.Brand;
import com.amalitech.gpuconfigurator.repository.BrandRepository;
import com.amalitech.gpuconfigurator.service.brand.BrandServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandServiceImpl brandService;


    @Test
    public void testCreateBrandSuccess() {
        BrandRequest brandRequest = new BrandRequest("brandName");
        UUID brandId = UUID.randomUUID();

        Brand brand = Brand.builder().name(brandRequest.name()).id(brandId).build();

        when(brandService.createBrand(brandRequest)).thenReturn(brand);

        Brand result = brandService.createBrand(brandRequest);


        assertEquals(brandRequest.name(), result.getName());
        assertEquals(brandId.toString(), result.getId().toString());
    }



}
