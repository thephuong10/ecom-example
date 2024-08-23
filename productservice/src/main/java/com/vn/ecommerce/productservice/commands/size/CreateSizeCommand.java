package com.vn.ecommerce.productservice.commands.size;

import com.vn.ecommerce.commonservice.exceptions.EntityAlreadyExistsException;
import com.vn.ecommerce.commonservice.helpers.StringHelper;
import com.vn.ecommerce.productservice.models.entities.SizeEntity;
import com.vn.ecommerce.productservice.models.requests.CreateSizeRequest;
import com.vn.ecommerce.productservice.repositories.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CreateSizeCommand {

    private final SizeRepository sizeRepository;

    @Autowired
    public CreateSizeCommand(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Transactional
    public Mono<UUID> handle(CreateSizeRequest request){
        return sizeRepository
                .existsByNameAndType(request.getName(),request.getType())
                .flatMap(existed->{
                    if(!existed){
                        SizeEntity entity = new SizeEntity();
                        entity.setName(request.getName());
                        entity.setSlug(StringHelper.genericSlugFromString(request.getName()));
                        entity.setType(request.getType());
                        return sizeRepository
                                .saveOrUpdate(entity)
                                .map(SizeEntity::getId);
                    }
                    return Mono.error(new EntityAlreadyExistsException(request.getName()));
                });

    }


}