package com.vn.ecommerce.productservice.commands.product;

import com.vn.ecommerce.commonservice.exceptions.BadRequestException;
import com.vn.ecommerce.commonservice.exceptions.NotFoundException;
import com.vn.ecommerce.productservice.converters.ProductConverter;
import com.vn.ecommerce.productservice.converters.ProductVariantConverter;
import com.vn.ecommerce.productservice.models.entities.*;
import com.vn.ecommerce.productservice.models.requests.CreateProductColorVariantRequest;
import com.vn.ecommerce.productservice.models.requests.CreateProductRequest;
import com.vn.ecommerce.productservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Component
@PropertySource("classpath:message.properties")
public class CreateProductCommand {


    @Value("${promotion.expired}")
    private String promotionExpired;

    @Value("${sys.invalidRequest}")
    private String invalidRequest;

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final CategoryRepository categoryRepository;
    private final ProductColorVariantRepository productColorVariantRepository;
    private final ProductSizeVariantRepository productSizeVariantRepository;

    private final ProductConverter productConverter;

    private final ProductVariantConverter productVariantConverter;


    @Autowired
    public CreateProductCommand(ProductRepository productRepository,
                                PromotionRepository promotionRepository,
                                CategoryRepository categoryRepository,
                                ProductColorVariantRepository productColorVariantRepository,
                                ProductSizeVariantRepository productSizeVariantRepository,
                                ProductConverter productConverter,
                                ProductVariantConverter productVariantConverter) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.categoryRepository = categoryRepository;
        this.productColorVariantRepository = productColorVariantRepository;
        this.productSizeVariantRepository = productSizeVariantRepository;
        this.productConverter = productConverter;
        this.productVariantConverter = productVariantConverter;
    }

    @Transactional
    public Mono<UUID> handle(CreateProductRequest request) {
        ProductEntity entity = initialProduct(request);
        return checkCategory(entity)
                .then(checkPromotion(entity))
                .then(productRepository.saveOrUpdate(entity))
                .map(ProductEntity::getId)
                .flatMap(productId->{
                    return Flux
                            .fromIterable(request.getChilds())
                            .flatMap(child->{
                                ProductColorVariantEntity productColorVariantEntity = productVariantConverter.toEntity(child);
                                productColorVariantEntity.setProductId(productId);
                                return productColorVariantRepository
                                        .saveOrUpdate(productColorVariantEntity)
                                        .map(ProductColorVariantEntity::getId)
                                        .flatMap(productColorVariantId->{
                                            return Flux
                                                    .fromIterable(child.getVariants())
                                                    .flatMap(variant->{
                                                        ProductSizeVariantEntity productSizeVariantEntity = productVariantConverter.toEntity(variant);
                                                        productSizeVariantEntity.setProductColorVariantId(productColorVariantId);
                                                        return productSizeVariantRepository
                                                                .saveOrUpdate(productSizeVariantEntity)
                                                                .then();
                                                    }).then();
                                        }).then();

                            }).then(Mono.just(productId));
                });
    }
    private Mono<ProductEntity> checkPromotion(ProductEntity entity){
        if(entity.getPromotionId() != null){
            return promotionRepository
                    .findById(entity.getPromotionId())
                    .flatMap(promotion -> {
                        LocalDateTime currentDate = LocalDateTime.now();
                        if(!promotion.getEndDate().isAfter(currentDate)){
                            return Mono.error(new BadRequestException(promotionExpired));
                        }
                        if (promotion.getStartDate().isBefore(currentDate)){
                            BigDecimal discount = entity.getPrice().multiply(BigDecimal.valueOf(promotion.getDiscount()));
                            entity.setPrice(entity.getPriceOriginal().subtract(discount));
                            entity.setDiscount(promotion.getDiscount());
                        }
                        return Mono.just(entity);
                    })
                    .switchIfEmpty(Mono.defer(()->Mono.error(new NotFoundException(entity.getPromotionId().toString()))));
        } else {
            return Mono.just(entity);
        }

    }
    private Mono<ProductEntity>checkCategory(ProductEntity entity){
        return categoryRepository
                .findById(entity.getCategoryId())
                .map(c->entity)
                .switchIfEmpty(Mono.defer(()->Mono.error(new NotFoundException(entity.getCategoryId().toString()))));
    }
    private ProductEntity initialProduct(CreateProductRequest request){
        if(request.getChilds().isEmpty()){
            throw new BadRequestException(invalidRequest);
        }
        LocalDateTime currentDate = LocalDateTime.now();
        ProductEntity entity = productConverter.toEntity(request);
        entity.setSlug(request.getName());
        entity.setImage(request.getChilds().get(0).getImage());
        entity.setPrice(entity.getPriceOriginal());
        entity.setLastModifierUserId(request.getCreatorUserId());
        entity.setCreationTime(currentDate);
        entity.setLastModificationTime(currentDate);
        entity.setQuantity(getQuantity(request));
        return entity;
    }
    private Integer getQuantity(CreateProductRequest request){
        AtomicReference<Integer> quantity= new AtomicReference<>(0);
        List<CreateProductColorVariantRequest>childs = request.getChilds();
        childs.forEach(child->{
            child.setQuantity(0);
            child.getVariants().forEach(variant->{
                child.setQuantity(child.getQuantity() + variant.getQuantity());
            });
            quantity.set(quantity.get() + child.getQuantity());
        });
        return quantity.get();
    }

}
