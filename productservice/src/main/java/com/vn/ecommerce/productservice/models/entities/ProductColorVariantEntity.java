package com.vn.ecommerce.productservice.models.entities;

import com.vn.ecommerce.productservice.constants.TableNameConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(TableNameConstant.PRODUCT_VARIANT_COLOR)
public class ProductColorVariantEntity extends BaseEntity {
    @Column("productId")
    private UUID productId;
    private String name;
    private String image;
    private Integer quantity;
}
