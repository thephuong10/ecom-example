package com.vn.ecommerce.productservice.repositories;

import com.vn.ecommerce.productservice.models.entities.ProductSizeVariantEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductSizeVariantRepository extends ReactiveCrudRepository<ProductSizeVariantEntity, UUID>,
        BaseRepository<ProductSizeVariantEntity>{

    Flux<ProductSizeVariantEntity> findAllByProductColorVariantId(UUID productColorVariantId);
}