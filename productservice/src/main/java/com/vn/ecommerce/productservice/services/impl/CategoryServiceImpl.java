package com.vn.ecommerce.productservice.services.impl;

import com.vn.ecommerce.productservice.models.entities.CategoryEntity;
import com.vn.ecommerce.productservice.services.CategoryService;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final String STORED_PROCEDURE_CALL = "EXEC sp_GetCategoryById :categoryId";

    private final DatabaseClient databaseClient;

    public CategoryServiceImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }
    @Override
    public Mono<List<CategoryEntity>> getCategoryHierarchy(UUID id) {
        return databaseClient
                .sql(STORED_PROCEDURE_CALL)
                .bind("categoryId",id)
                .map(row->{
                    CategoryEntity category = new CategoryEntity();
                    category.setId(row.get("id",UUID.class));
                    category.setParentId(row.get("parentId",UUID.class));
                    category.setName(row.get("name",String.class));
                    category.setSlug(row.get("slug",String.class));
                    category.setLevel(row.get("level", Integer.class));
                    return category;
                }).all().collectList();
    }
}
