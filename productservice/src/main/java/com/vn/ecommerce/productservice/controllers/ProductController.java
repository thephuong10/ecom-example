package com.vn.ecommerce.productservice.controllers;

import com.vn.ecommerce.productservice.commands.product.CreateProductCommand;
import com.vn.ecommerce.productservice.commands.product.UpdateProductCommand;
import com.vn.ecommerce.productservice.models.requests.CreateProductRequest;
import com.vn.ecommerce.productservice.models.requests.UpdateProductRequest;
import com.vn.ecommerce.productservice.queries.product.GetOneProductQuery;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/product-service/v1/product")
public class ProductController extends BaseController {

    private final CreateProductCommand createProductCommand;

    private final UpdateProductCommand updateProductCommand;

    private final GetOneProductQuery getOneProductQuery;

    @Autowired
    public ProductController(CreateProductCommand createProductCommand, UpdateProductCommand updateProductCommand, GetOneProductQuery getOneProductQuery) {
        this.createProductCommand = createProductCommand;
        this.updateProductCommand = updateProductCommand;
        this.getOneProductQuery = getOneProductQuery;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>>create(@RequestBody CreateProductRequest request){
        return response(createProductCommand.handle(request));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>>update(@RequestBody UpdateProductRequest request){
        return response(updateProductCommand.handle(request));
    }

    @GetMapping("/one/{id}")
    public Mono<ResponseEntity<?>>getById(@PathVariable UUID id){
        return response(getOneProductQuery.handle(id));
    }
}
