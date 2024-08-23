package com.vn.ecommerce.productservice.repositories;

import com.vn.ecommerce.productservice.models.entities.CategoryEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CategoryRepository extends ReactiveCrudRepository<CategoryEntity, UUID>,
        BaseRepository<CategoryEntity> {

    Mono<Boolean>existsByName(String name);

}
