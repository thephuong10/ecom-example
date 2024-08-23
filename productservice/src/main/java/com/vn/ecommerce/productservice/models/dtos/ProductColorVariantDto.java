package com.vn.ecommerce.productservice.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductColorVariantDto extends BaseDto {
    private String fullName;
    private String name;
    private String image;
    private Integer quantity;
    private List<ProductSizeVariantDto>variants;
}
