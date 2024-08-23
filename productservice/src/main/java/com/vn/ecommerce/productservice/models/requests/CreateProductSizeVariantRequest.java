package com.vn.ecommerce.productservice.models.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductSizeVariantRequest {
    private String name;
    private Integer quantity;
}
