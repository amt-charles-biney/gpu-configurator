package com.amalitech.gpuconfigurator.service.brand;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.brand.BrandDto;
import com.amalitech.gpuconfigurator.dto.brand.BrandRequest;
import com.amalitech.gpuconfigurator.exception.CustomExceptionHandler;
import com.amalitech.gpuconfigurator.model.Brand;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.BrandRepository;
import com.amalitech.gpuconfigurator.service.attribute.AttributeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final AttributeServiceImpl attributeService;


    @Override
    public List<BrandDto> getAllBrands() {
        List<AttributeOption> brands = attributeService.getAllAttributeOptions();

        return brands.stream()
                .map(brand -> BrandDto.builder()
                        .name(brand.getBrand().toLowerCase())
                        .id(String.valueOf(brand.getId()))
                        .thumbnail(brand.getMedia())
                        .description(brand.getAttribute().getDescription())
                        .build())
                .collect(Collectors.toMap(
                        BrandDto::name,
                        brand -> brand,
                        (existing, replacement) -> existing))
                .values().stream()
                .toList();
    }


}
