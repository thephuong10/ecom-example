package com.vn.ecommerce.productservice.repositories;

import com.vn.ecommerce.productservice.models.entities.PromotionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface PromotionRepository extends ReactiveCrudRepository<PromotionEntity, UUID>,
        BaseRepository<PromotionEntity>{
}