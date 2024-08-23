package com.vn.ecommerce.productservice.repositories;

import com.vn.ecommerce.productservice.models.entities.BaseEntity;
import reactor.core.publisher.Mono;

public interface BaseRepository<T extends BaseEntity> {
    Mono<T> saveOrUpdate(T entity);
}
