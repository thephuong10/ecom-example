package com.vn.ecommerce.productservice.repositories;

import com.vn.ecommerce.productservice.models.entities.ProductColorVariantEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductColorVariantRepository extends ReactiveCrudRepository<ProductColorVariantEntity, UUID>,
        BaseRepository<ProductColorVariantEntity>{

    Flux<ProductColorVariantEntity> findAllByProductId(UUID productId);
}