package com.vn.ecommerce.productservice.queries.category;

import com.vn.ecommerce.productservice.models.dtos.CategoryDto;
import com.vn.ecommerce.productservice.models.entities.CategoryEntity;
import com.vn.ecommerce.productservice.repositories.CategoryRepository;
import com.vn.ecommerce.productservice.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class GetOneCategoryQuery {

    private final CategoryRepository categoryRepository;

    private final CategoryService categoryService;

    @Autowired
    public GetOneCategoryQuery(CategoryRepository categoryRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    public Mono<CategoryDto>handle(UUID id){
        return categoryService
                .getCategoryHierarchy(id)
                .map(categories->{
                    List<CategoryDto>result = new ArrayList<>();
                    for (int i = 0; i < categories.size() - 1; i++) {
                        CategoryEntity entity_i = categories.get(i);
                        CategoryDto dto_i = convertEntityToDto(entity_i);
                        List<CategoryDto>parents = new ArrayList<>();
                        for (int j = i + 1; j < categories.size(); j++) {
                            CategoryEntity entity_j = categories.get(j);
                            CategoryDto dto_j = convertEntityToDto(entity_j);
                            parents.add(dto_j);
                        }
                        dto_i.setParents(parents);
                        result.add(dto_i);
                    }
                    CategoryDto category;

                    if(!result.isEmpty()){
                        category = result.get(0);

                        for (int i = 1; i < result.size(); i++) {
                            category.getParents().set(i-1,result.get(i));
                        }
                    } else {
                        category = convertEntityToDto(categories.get(0));
                    }
                    return category;
                });
    }

    private CategoryDto convertEntityToDto(CategoryEntity entity){
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setSlug(entity.getSlug());
        dto.setName(entity.getName());
        dto.setLevel(entity.getLevel());
        return dto;
    }

}
