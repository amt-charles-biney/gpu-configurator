package com.amalitech.gpuconfigurator.service.brand;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.brand.BrandDto;

import java.util.List;
import java.util.UUID;

public interface BrandService {
    BrandDto createBrand(BrandDto brandDto);

    List<BrandDto> getAllBrands();

    GenericResponse deleteBrand(UUID id);
}
