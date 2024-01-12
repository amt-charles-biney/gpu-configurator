package com.amalitech.gpuconfigurator.mapper;

import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.CategoryDocument;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "categoryName")
    CategoryDocument toCategoryDocument(Category category);
}