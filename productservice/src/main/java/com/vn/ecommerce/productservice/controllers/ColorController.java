package com.vn.ecommerce.productservice.controllers;

import com.vn.ecommerce.productservice.commands.color.CreateColorCommand;
import com.vn.ecommerce.productservice.models.requests.CreateColorRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/product-service/v1/color")
public class ColorController extends BaseController {

    private final CreateColorCommand createColorCommand;

    @Autowired
    public ColorController(CreateColorCommand createColorCommand) {
        this.createColorCommand = createColorCommand;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> create(@RequestBody CreateColorRequest request){
        return response(createColorCommand.handle(request));
    }

}
