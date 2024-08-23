package com.vn.ecommerce.productservice.models.entities;

import com.vn.ecommerce.productservice.constants.TableNameConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table(TableNameConstant.SIZE)
public class SizeEntity extends BaseEntity {
    private String name;
    private String slug;
    private String type;
}
