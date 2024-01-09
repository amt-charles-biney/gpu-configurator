package com.amalitech.gpuconfigurator.mapper;

import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.ProductDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDocument toProductDocument(Product product);
}
