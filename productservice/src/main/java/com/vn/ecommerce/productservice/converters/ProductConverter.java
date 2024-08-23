package com.vn.ecommerce.productservice.converters;

import com.vn.ecommerce.productservice.models.dtos.ProductDto;
import com.vn.ecommerce.productservice.models.entities.ProductEntity;
import com.vn.ecommerce.productservice.models.requests.CreateProductRequest;
import com.vn.ecommerce.productservice.models.requests.UpdateProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductConverter {
    ProductDto toDto(ProductEntity entity);
    ProductEntity toEntity(CreateProductRequest request);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "name", source = "source.name")
    @Mapping(target = "image", source = "source.image")
    @Mapping(target = "priceOriginal", source = "source.priceOriginal")
    @Mapping(target = "categoryId", source = "source.categoryId")
    @Mapping(target = "promotionId", source = "source.promotionId")
    @Mapping(target = "creatorUserId", source = "source.creatorUserId")
    ProductEntity toEntity(UpdateProductRequest source,@MappingTarget ProductEntity target);

}
