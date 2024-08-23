package com.vn.ecommerce.productservice.models.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductColorVariantRequest {
    private String name;
    private String image;
    private Integer quantity;
    private List<CreateProductSizeVariantRequest> variants;
}
