package com.vn.ecommerce.productservice.repositories;

import com.vn.ecommerce.productservice.models.entities.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, UUID>,
                                           BaseRepository<ProductEntity>{
}
