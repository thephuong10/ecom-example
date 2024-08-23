package com.vn.ecommerce.productservice.controllers;

import com.vn.ecommerce.productservice.commands.category.CreateCategoryCommand;
import com.vn.ecommerce.productservice.models.requests.CreateCategoryRequest;
import com.vn.ecommerce.productservice.queries.category.GetAllCategoryQuery;
import com.vn.ecommerce.productservice.queries.category.GetOneCategoryQuery;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/product-service/v1/category")
public class CategoryController extends BaseController {

    private final CreateCategoryCommand createCategoryCommand;

    private final GetAllCategoryQuery getAllCategoryQuery;

    private final GetOneCategoryQuery getOneCategoryQuery;

    @Autowired
    public CategoryController(CreateCategoryCommand createCategoryCommand, GetAllCategoryQuery getAllCategoryQuery, GetOneCategoryQuery getOneCategoryQuery) {
        this.createCategoryCommand = createCategoryCommand;
        this.getAllCategoryQuery = getAllCategoryQuery;
        this.getOneCategoryQuery = getOneCategoryQuery;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> create(@RequestBody CreateCategoryRequest request){
        return response(createCategoryCommand.handle(request));
    }

    @GetMapping("/all")
    public Mono<ResponseEntity<?>> getAll(){
        return response(getAllCategoryQuery.handle());
    }

    @GetMapping("/one/{id}")
    public Mono<ResponseEntity<?>> getById(@PathVariable UUID id){
        return response(getOneCategoryQuery.handle(id));
    }


}
