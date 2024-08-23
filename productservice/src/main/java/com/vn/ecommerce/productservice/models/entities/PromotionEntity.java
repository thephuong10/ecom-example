package com.vn.ecommerce.productservice.models.entities;

import com.vn.ecommerce.productservice.constants.TableNameConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(TableNameConstant.PROMOTION)
public class PromotionEntity extends BaseEntity {

    private String name;
    private String slug;
    private String image;
    private String description;
    private Double discount;
    @Column("creationTime")
    private LocalDateTime creationTime;
    @Column("lastModificationTime")
    private LocalDateTime lastModificationTime;
    @Column("creatorUserId")
    private UUID creatorUserId;
    @Column("lastModifierUserId")
    private UUID lastModifierUserId;
    @Column("startDate")
    private LocalDateTime startDate;
    @Column("endDate")
    private LocalDateTime endDate;

}
