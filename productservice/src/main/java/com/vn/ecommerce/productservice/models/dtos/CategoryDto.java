package com.vn.ecommerce.productservice.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto extends BaseDto {
    private String name;
    private String slug;
    private Integer level;
    private UUID parentId;
    private List<CategoryDto> parents;
}
