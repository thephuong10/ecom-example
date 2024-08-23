package com.vn.ecommerce.productservice.commands.category;

import com.vn.ecommerce.commonservice.exceptions.BadRequestException;
import com.vn.ecommerce.commonservice.exceptions.EntityAlreadyExistsException;
import com.vn.ecommerce.commonservice.exceptions.NotFoundException;
import com.vn.ecommerce.commonservice.helpers.StringHelper;
import com.vn.ecommerce.productservice.converters.CategoryConverter;
import com.vn.ecommerce.productservice.models.entities.CategoryEntity;
import com.vn.ecommerce.productservice.models.requests.CreateCategoryRequest;
import com.vn.ecommerce.productservice.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@PropertySource("classpath:message.properties")
public class CreateCategoryCommand {

    private String notFoundParent = "parent id %s";


    @Value("${category.level.invalid}")
    private String levelInvalid;

    private final CategoryRepository categoryRepository;

    private final CategoryConverter categoryConverter;

    @Autowired
    public CreateCategoryCommand(CategoryRepository categoryRepository, CategoryConverter categoryConverter) {
        this.categoryRepository = categoryRepository;
        this.categoryConverter = categoryConverter;
    }


    @Transactional
    public Mono<UUID> handle(CreateCategoryRequest request){
        return categoryRepository
                .existsByName(request.getName())
                .flatMap(existed->{
                    if(!existed){
                        if(request.getParentId() != null){
                            return categoryRepository
                                    .findById(request.getParentId())
                                    .flatMap(parentEntity->{
                                        if(parentEntity.getLevel() > request.getLevel() || parentEntity.getLevel() + 1 != request.getLevel()){
                                            return Mono.error(new BadRequestException(levelInvalid));
                                        }
                                        CategoryEntity entity = categoryConverter.toEntity(request);
                                        entity.setSlug(StringHelper.genericSlugFromString(entity.getName()));
                                        return categoryRepository.saveOrUpdate(entity).map(CategoryEntity::getId);
                                    })
                                    .switchIfEmpty(Mono.error(new NotFoundException(
                                            String.format(notFoundParent,request.getParentId())
                                    )));
                        } else {
                            CategoryEntity entity = new CategoryEntity();
                            entity.setName(request.getName());
                            entity.setSlug(StringHelper.genericSlugFromString(request.getName()));
                            entity.setLevel(0);
                            entity.setParentId(null);
                            return categoryRepository
                                    .saveOrUpdate(entity)
                                    .map(CategoryEntity::getId);
                        }
                    }
                    return Mono.error(new EntityAlreadyExistsException(request.getName()));
                });
    }


}
