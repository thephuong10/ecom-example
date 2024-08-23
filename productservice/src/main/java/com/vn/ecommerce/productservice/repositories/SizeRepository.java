package com.vn.ecommerce.productservice.repositories;

import com.vn.ecommerce.productservice.models.entities.SizeEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SizeRepository extends ReactiveCrudRepository<SizeEntity, UUID>,
        BaseRepository<SizeEntity>{
    Mono<Boolean> existsByNameAndType(String name,String type);
}
