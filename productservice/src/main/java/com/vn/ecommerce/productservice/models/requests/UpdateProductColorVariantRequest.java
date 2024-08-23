package com.vn.ecommerce.productservice.models.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductColorVariantRequest {
    private UUID id;
    private String name;
    private String image;
    private Integer quantity;
    private List<UpdateProductSizeVariantRequest>variants;
}
