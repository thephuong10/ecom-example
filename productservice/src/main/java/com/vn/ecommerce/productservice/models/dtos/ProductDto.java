package com.vn.ecommerce.productservice.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto extends BaseDto {
    private String name;
    private String slug;
    private String image;
    private CategoryDto category;
    private Integer quantity;
    private List<ProductColorVariantDto> childs;
    private BigDecimal price;
    private BigDecimal priceOriginal;
    private Double discount;
}
