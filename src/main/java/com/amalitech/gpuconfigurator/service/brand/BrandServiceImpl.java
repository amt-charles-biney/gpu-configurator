package com.amalitech.gpuconfigurator.service.brand;

import com.amalitech.gpuconfigurator.dto.brand.BrandDto;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.service.attribute.AttributeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final AttributeServiceImpl attributeService;


    @Override
    public List<BrandDto> getAllBrands() {
        List<AttributeOption> brands = attributeService.getAllAttributeOptions();

        return brands.stream().map(
                brand -> BrandDto.builder()
                        .name(brand.getBrand().toLowerCase())
                        .id(String.valueOf(brand.getId()))
                        .thumbnail(brand.getMedia())
                        .description(brand.getAttribute().getDescription())
                        .build()
        ).distinct().toList();
    }


}
