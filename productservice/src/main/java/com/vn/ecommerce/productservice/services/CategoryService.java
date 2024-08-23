package com.vn.ecommerce.productservice.services;

import com.vn.ecommerce.productservice.models.entities.CategoryEntity;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Mono<List<CategoryEntity>>getCategoryHierarchy(UUID id);
}
