package com.vn.ecommerce.productservice.commands.color;

import com.vn.ecommerce.commonservice.exceptions.EntityAlreadyExistsException;
import com.vn.ecommerce.commonservice.helpers.StringHelper;
import com.vn.ecommerce.productservice.models.entities.ColorEntity;
import com.vn.ecommerce.productservice.models.requests.CreateColorRequest;
import com.vn.ecommerce.productservice.repositories.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CreateColorCommand {

    private final ColorRepository colorRepository;

    @Autowired
    public CreateColorCommand(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public Mono<UUID>handle(CreateColorRequest request){
        return colorRepository
                .existsByName(request.getName())
                .flatMap(existed->{
                    if(!existed){
                        ColorEntity entity = new ColorEntity();
                        entity.setName(request.getName());
                        entity.setSlug(StringHelper.genericSlugFromString(request.getName()));
                        return colorRepository
                                .saveOrUpdate(entity)
                                .map(ColorEntity::getId);
                    }
                    return Mono.error(new EntityAlreadyExistsException(request.getName()));
                });

    }

}
