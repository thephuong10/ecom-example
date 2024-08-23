package com.vn.ecommerce.productservice.queries.category;

import com.vn.ecommerce.productservice.models.dtos.CategoryDto;
import com.vn.ecommerce.productservice.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class GetAllCategoryQuery {

    private final CategoryRepository categoryRepository;

    @Autowired
    public GetAllCategoryQuery(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Mono<List<CategoryDto>> handle() {
        return categoryRepository
                .findAll()
                .collectList()
                .map(categories->categories.stream().map(c->{
                    CategoryDto dto = new CategoryDto();
                    dto.setId(c.getId());
                    dto.setSlug(c.getSlug());
                    dto.setName(c.getName());
                    dto.setLevel(c.getLevel());
                    dto.setParentId(c.getParentId());
                    return dto;
                }).toList())
                .flatMap(categories -> {
                    List<CategoryDto> result = new ArrayList<>();
                    if (!categories.isEmpty()) {
                        int max = categories.stream().map(CategoryDto::getLevel).max(Integer::compare).get();
                        List<CategoryDto>parents = categories.stream().filter(c->c.getLevel()==0).toList();
                        for (CategoryDto parent : parents) {
                            categoryHierarchy(1, max, parent, categories);
                        }
                        return Mono.just(parents);
                    }
                    return Mono.just(result);
                });
    }

    private void categoryHierarchy(int level,int max,CategoryDto current,List<CategoryDto> categories){
        List<CategoryDto>parents = categories
                .stream()
                .filter(c->c.getLevel()==level && current.getId().compareTo(c.getParentId())==0)
                .toList();
        current.setParents(parents);
        if(level < max){
            for (CategoryDto parent : parents) {
                categoryHierarchy(level + 1, max, parent, categories);
            }
        }
    }

}
