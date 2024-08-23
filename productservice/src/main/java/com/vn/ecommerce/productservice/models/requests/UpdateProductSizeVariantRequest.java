package com.vn.ecommerce.productservice.models.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductSizeVariantRequest {
    private UUID id;
    private String name;
    private Integer quantity;
}
