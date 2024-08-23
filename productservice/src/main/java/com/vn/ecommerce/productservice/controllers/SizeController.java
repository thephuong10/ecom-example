package com.vn.ecommerce.productservice.controllers;

import com.vn.ecommerce.productservice.commands.size.CreateSizeCommand;
import com.vn.ecommerce.productservice.models.requests.CreateSizeRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/product-service/v1/size")
@Valid
public class SizeController extends BaseController {

    private final CreateSizeCommand createSizeCommand;

    @Autowired
    public SizeController(CreateSizeCommand createSizeCommand) {
        this.createSizeCommand = createSizeCommand;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> create(@RequestBody CreateSizeRequest request){
        return response(createSizeCommand.handle(request));
    }

}
