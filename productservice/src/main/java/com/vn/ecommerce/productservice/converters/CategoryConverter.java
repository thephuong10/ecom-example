package com.vn.ecommerce.productservice.converters;

import com.vn.ecommerce.productservice.models.dtos.CategoryDto;
import com.vn.ecommerce.productservice.models.entities.CategoryEntity;
import com.vn.ecommerce.productservice.models.requests.CreateCategoryRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryConverter {
    CategoryDto toDto(CategoryEntity entity);
    CategoryEntity toEntity(CreateCategoryRequest request);
}
