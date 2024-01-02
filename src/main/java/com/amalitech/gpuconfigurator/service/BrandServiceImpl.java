package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.brand.BrandDto;
import com.amalitech.gpuconfigurator.model.Brand;
import com.amalitech.gpuconfigurator.model.ProductBrand;
import com.amalitech.gpuconfigurator.repository.BrandRepository;
import com.amalitech.gpuconfigurator.repository.ProductBrandRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl {

    private final BrandRepository brandRepository;
    private final ProductBrandRepository productBrandRepository;

    @Override
    public BrandDto createBrand(BrandDto brandDto) {
        Brand newBrand = Brand
                .builder()
                .name(brandDto.name())
                .build();

        return new BrandDto(newBrand.getName(), newBrand.getId().toString());
    }

    @Override
    public List<BrandDto> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();

        return brands
                .stream()
                .map(brand -> new BrandDto(brand.getName(), brand.getId().toString()))
                .toList();
    }

    @Override
    public GenericResponse deleteBrand(UUID id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("brand cannot be found"));
        brandRepository.deleteById(id);
        return new GenericResponse(204, "brand has been deleted successfully");
    }

    public BrandDto getBrandByProductId(UUID productId) {
        ProductBrand productBrand = productBrandRepository.findByProductId(productId).orElseThrow(() -> new EntityNotFoundException("could not get the product brand"));
        Brand brand = brandRepository.findById(productBrand.getBrandId()).orElseThrow(() -> new EntityNotFoundException("could not get brand from product"));

        return new BrandDto(brand.getName(), brand.getId().toString());

    }

}
