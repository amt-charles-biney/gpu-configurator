package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.brand.BrandDto;
import com.amalitech.gpuconfigurator.dto.brand.BrandRequest;
import com.amalitech.gpuconfigurator.model.Brand;
import com.amalitech.gpuconfigurator.service.brand.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @CrossOrigin
    @GetMapping("/v1/brand")
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        List<BrandDto> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

}
