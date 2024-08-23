package com.vn.ecommerce.productservice.models.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductRequest {
    private UUID id;
    private String name;
    private String image;
    private BigDecimal priceOriginal;
    private UUID categoryId;
    private UUID promotionId;
    private UUID creatorUserId;
    private List<UpdateProductColorVariantRequest>childs;
}
