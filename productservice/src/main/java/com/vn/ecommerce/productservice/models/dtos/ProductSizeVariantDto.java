package com.vn.ecommerce.productservice.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductSizeVariantDto extends BaseDto {
    private String fullName;
    private String name;
    private Integer quantity;
}
