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
@Table(TableNameConstant.CATEGORY)
public class CategoryEntity extends BaseEntity {
    private String name;
    private String slug;
    private Integer level;
    @Column("parentId")
    private UUID parentId;
}
