package com.vn.ecommerce.productservice.models.entities;

import com.vn.ecommerce.productservice.constants.TableNameConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(TableNameConstant.PRODUCT)
public class ProductEntity extends BaseEntity {

    private String name;
    private String slug;
    private String image;
    @Column("categoryId")
    private UUID categoryId;
    @Column("creationTime")
    private LocalDateTime creationTime;
    @Column("lastModificationTime")
    private LocalDateTime lastModificationTime;
    @Column("creatorUserId")
    private UUID creatorUserId;
    @Column("lastModifierUserId")
    private UUID lastModifierUserId;
    @Column("ratingAverage")
    private Double ratingAverage;
    private BigDecimal price;
    @Column("priceOriginal")
    private BigDecimal priceOriginal;
    private Double discount;
    @Column("promotionId")
    private UUID promotionId;
    private Integer quantity;

}
