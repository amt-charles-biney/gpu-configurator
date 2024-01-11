package com.amalitech.gpuconfigurator.mapper;

import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.ProductDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "isFeatured", source = "featured")
    ProductDocument toProductDocument(Product product);
}