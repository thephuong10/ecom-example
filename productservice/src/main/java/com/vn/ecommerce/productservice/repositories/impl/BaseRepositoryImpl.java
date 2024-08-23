package com.vn.ecommerce.productservice.repositories.impl;

import com.vn.ecommerce.productservice.models.entities.BaseEntity;
import com.vn.ecommerce.productservice.repositories.BaseRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class BaseRepositoryImpl<T extends BaseEntity> implements BaseRepository<T> {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public BaseRepositoryImpl(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Mono<T> saveOrUpdate(T entity) {
        return Mono.defer(()->{
            if (entity != null) {
                if (entity.getId() != null) {
                    return update(entity);
                }
                return save(entity);
            }
            throw new RuntimeException("");
        });
    }

    private Mono<T> save(T entity) {
        entity.setId(UUID.randomUUID());
        return r2dbcEntityTemplate.insert(entity);
    }

    private Mono<T> update(T entity) {
        return r2dbcEntityTemplate
                .select(entity.getClass())
                .matching(Query
                        .query(Criteria.where("id").is(entity.getId()))
                )
                .one()
                .flatMap(_entity -> {
                    return r2dbcEntityTemplate.update(entity);
                })
                .switchIfEmpty(Mono.defer(() -> r2dbcEntityTemplate.insert(entity)));
    }
}

