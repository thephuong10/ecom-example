package com.vn.ecommerce.productservice.converters;

import com.vn.ecommerce.productservice.models.dtos.ProductColorVariantDto;
import com.vn.ecommerce.productservice.models.dtos.ProductSizeVariantDto;
import com.vn.ecommerce.productservice.models.entities.ProductColorVariantEntity;
import com.vn.ecommerce.productservice.models.entities.ProductSizeVariantEntity;
import com.vn.ecommerce.productservice.models.requests.CreateProductColorVariantRequest;
import com.vn.ecommerce.productservice.models.requests.CreateProductSizeVariantRequest;
import com.vn.ecommerce.productservice.models.requests.UpdateProductColorVariantRequest;
import com.vn.ecommerce.productservice.models.requests.UpdateProductSizeVariantRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductVariantConverter {
    ProductColorVariantDto toDto(ProductColorVariantEntity entity);
    ProductColorVariantEntity toEntity(CreateProductColorVariantRequest request);
    @Mapping(target = "id",ignore = true)
    ProductColorVariantEntity toEntity(UpdateProductColorVariantRequest request);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "name", source = "source.name")
    @Mapping(target = "image", source = "source.image")
    @Mapping(target = "quantity", source = "source.quantity")
    ProductColorVariantEntity toEntity(UpdateProductColorVariantRequest source,
                                       @MappingTarget ProductColorVariantEntity target);


    ProductSizeVariantDto toDto(ProductSizeVariantEntity entity);
    ProductSizeVariantEntity toEntity(CreateProductSizeVariantRequest request);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "name", source = "source.name")
    @Mapping(target = "quantity", source = "source.quantity")
    ProductSizeVariantEntity toEntity(UpdateProductSizeVariantRequest source,
                                      @MappingTarget ProductSizeVariantEntity target);

    @Mapping(target = "id",ignore = true)
    ProductSizeVariantEntity toEntity(UpdateProductSizeVariantRequest request);

}
