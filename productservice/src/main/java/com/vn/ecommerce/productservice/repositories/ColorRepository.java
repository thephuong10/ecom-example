package com.vn.ecommerce.productservice.repositories;

import com.vn.ecommerce.productservice.models.entities.ColorEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ColorRepository extends ReactiveCrudRepository<ColorEntity, UUID>,
        BaseRepository<ColorEntity>{
    Mono<Boolean>existsByName(String name);
}
