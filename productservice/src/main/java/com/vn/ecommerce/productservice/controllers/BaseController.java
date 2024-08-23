package com.vn.ecommerce.productservice.controllers;

import com.vn.ecommerce.commonservice.models.ResponseModel;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public class BaseController {
    public <T> Mono<ResponseEntity<?>> response(Mono<T>mono){
        ResponseModel<T> responseModel = new ResponseModel<>();
        return mono
                .map(result -> {
                    responseModel.setStatus(200);
                    responseModel.setMessage(null);
                    responseModel.setData(result);
                    return ResponseEntity.ok(responseModel);
                });
    }

}
