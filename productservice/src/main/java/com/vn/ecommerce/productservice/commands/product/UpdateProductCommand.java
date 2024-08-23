package com.vn.ecommerce.productservice.commands.product;

import com.vn.ecommerce.productservice.converters.ProductConverter;
import com.vn.ecommerce.productservice.converters.ProductVariantConverter;
import com.vn.ecommerce.productservice.models.entities.ProductColorVariantEntity;
import com.vn.ecommerce.productservice.models.entities.ProductEntity;
import com.vn.ecommerce.productservice.models.entities.ProductSizeVariantEntity;
import com.vn.ecommerce.productservice.models.requests.*;
import com.vn.ecommerce.productservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
@PropertySource("classpath:message.properties")
public class UpdateProductCommand {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final CategoryRepository categoryRepository;
    private final ProductColorVariantRepository productColorVariantRepository;
    private final ProductSizeVariantRepository productSizeVariantRepository;
    private final ProductConverter productConverter;
    private final ProductVariantConverter productVariantConverter;

    @Autowired
    public UpdateProductCommand(ProductRepository productRepository,
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
    public Mono<UUID> handle(UpdateProductRequest request) {

        return productRepository
                .findById(request.getId())
                .flatMap(productEntity -> {
                    productEntity = productConverter.toEntity(request, productEntity);
                    Mono<List<ProductColorVariantEntity>> colorsMono = productColorVariantRepository
                            .findAllByProductId(productEntity.getId()).collectList().share();
                    ProductEntity finalProductEntity = productEntity;
                    return updateProductColorVariants(
                            colorsMono,
                            Mono.just(request.getChilds()),
                            productEntity.getId()
                    ).then(Mono.defer(()->{
                        finalProductEntity.setQuantity(setQuantityForProduct(request));
                        finalProductEntity.setLastModificationTime(LocalDateTime.now());
                        return productRepository.saveOrUpdate(finalProductEntity).thenReturn(finalProductEntity.getId());
                    }));
                });

    }

    private Integer setQuantityForProduct(UpdateProductRequest request){
        AtomicReference<Integer> quantity= new AtomicReference<>(0);
        List<UpdateProductColorVariantRequest>childs = request.getChilds();
        childs.forEach(child->{
            child.setQuantity(0);
            child.getVariants().forEach(variant->{
                child.setQuantity(child.getQuantity() + variant.getQuantity());
            });
            quantity.set(quantity.get() + child.getQuantity());
        });
        return quantity.get();
    }

    private Mono<Void> updateProductColorVariants(Mono<List<ProductColorVariantEntity>> childEntitiesMono,
                                                  Mono<List<UpdateProductColorVariantRequest>> childRequestsMono,
                                                  UUID productId) {
        return Mono.zip(childEntitiesMono, childRequestsMono)
                .flatMap(mono -> {
                    Set<UUID> ids = new HashSet<>();
                    List<ProductColorVariantEntity> childEntities = mono.getT1();
                    List<UpdateProductColorVariantRequest> childRequests = mono.getT2();
                    List<ProductColorVariantEntity> saveOrUpdates = new ArrayList<>();
                    List<UpdateProductColorVariantRequest> newColors = new ArrayList<>();
                    Map<UUID, List<UpdateProductSizeVariantRequest>> sizesRequest = new HashMap<>();
                    for (int i = 0; i < childRequests.size(); i++) {
                        UpdateProductColorVariantRequest childRequest = childRequests.get(i);
                        if (childRequest.getId() == null) {
                            newColors.add(childRequest);
                        } else {
                            for (int j = 0; j < childEntities.size(); j++) {
                                ProductColorVariantEntity childEntity = childEntities.get(j);
                                if (childEntity.getId().compareTo(childRequest.getId()) == 0) {
                                    ids.add(childEntity.getId());
                                    childEntity = productVariantConverter.toEntity(childRequest, childEntity);
                                    saveOrUpdates.add(childEntity);
                                    sizesRequest.put(childEntity.getId(), childRequest.getVariants());
                                    break;
                                }
                            }
                        }
                    }
                    List<ProductColorVariantEntity> removes = new ArrayList<>(childEntities
                            .stream()
                            .filter(r -> ids.stream().allMatch(s -> r.getId().compareTo(s) != 0))
                            .toList());
                    int length = Math.abs(removes.size() - saveOrUpdates.size());
                    if (length != 0) {
                        if (removes.size() > saveOrUpdates.size()) {
                            saveOrUpdates.addAll(Collections.nCopies(length, new ProductColorVariantEntity()));
                        } else {
                            removes.addAll(Collections.nCopies(length, new ProductColorVariantEntity()));
                        }
                    }
                    return Flux.zip(Flux.fromIterable(saveOrUpdates), Flux.fromIterable(removes))
                            .flatMap(flux -> {
                                ProductColorVariantEntity saveOrUpdate = flux.getT1();
                                ProductColorVariantEntity remove = flux.getT2();
                                if (saveOrUpdate.getId() == null){
                                    return removeAllByProductColorVariant(remove);
                                } else if(remove.getId() == null) {
                                    return updateProductSizeVariants(
                                            productSizeVariantRepository
                                                    .findAllByProductColorVariantId(saveOrUpdate.getId()).collectList(),
                                            Mono.just(sizesRequest.get(saveOrUpdate.getId())),
                                            saveOrUpdate.getId()
                                    ).then(Mono.defer(()->{
                                        saveOrUpdate.setQuantity(
                                                sizesRequest.get(saveOrUpdate.getId())
                                                        .stream()
                                                        .map(UpdateProductSizeVariantRequest::getQuantity)
                                                        .reduce(0,Integer::sum)
                                        );
                                        return productColorVariantRepository
                                                .saveOrUpdate(saveOrUpdate)
                                                .then();
                                    }));
                                }
                                return Mono.when(
                                        updateProductSizeVariants(
                                                productSizeVariantRepository
                                                        .findAllByProductColorVariantId(saveOrUpdate.getId()).collectList(),
                                                Mono.just(sizesRequest.get(saveOrUpdate.getId())),
                                                saveOrUpdate.getId()
                                        ),
                                        removeAllByProductColorVariant(remove)
                                ).then(Mono.defer(()->{
                                    saveOrUpdate.setQuantity(
                                            sizesRequest.get(saveOrUpdate.getId())
                                                    .stream()
                                                    .map(UpdateProductSizeVariantRequest::getQuantity)
                                                    .reduce(0,Integer::sum)
                                    );
                                    return productColorVariantRepository
                                            .saveOrUpdate(saveOrUpdate)
                                            .then();
                                }));
                            }).then(Mono.defer(() -> {
                                if (!newColors.isEmpty()) {
                                    return Flux
                                            .fromIterable(newColors)
                                            .flatMap(color ->{
                                                ProductColorVariantEntity productColorVariantEntity = productVariantConverter.toEntity(color);
                                                productColorVariantEntity.setProductId(productId);
                                                return productColorVariantRepository
                                                        .saveOrUpdate(productColorVariantEntity)
                                                        .map(ProductColorVariantEntity::getId)
                                                        .flatMap(productColorVariantId->{
                                                            return Flux
                                                                    .fromIterable(color.getVariants())
                                                                    .flatMap(variant->{
                                                                        ProductSizeVariantEntity productSizeVariantEntity = productVariantConverter.toEntity(variant);
                                                                        productSizeVariantEntity.setProductColorVariantId(productColorVariantId);
                                                                        return productSizeVariantRepository
                                                                                .saveOrUpdate(productSizeVariantEntity)
                                                                                .then();
                                                                    }).then();
                                                        }).then();

                                            }).then();
                                }
                                return Mono.empty();
                            }));
                });
    }

    private Mono<Void> updateProductSizeVariants(Mono<List<ProductSizeVariantEntity>> childEntitiesMono,
                                                 Mono<List<UpdateProductSizeVariantRequest>> childRequestsMono,
                                                 UUID productColorVariantId) {
        return Mono.zip(childEntitiesMono, childRequestsMono)
                .flatMap(mono -> {
                    Set<UUID> ids = new HashSet<>();
                    List<ProductSizeVariantEntity> childEntities = mono.getT1();
                    List<UpdateProductSizeVariantRequest> childRequests = mono.getT2();
                    List<ProductSizeVariantEntity> saveOrUpdates = new ArrayList<>();
                    List<UpdateProductSizeVariantRequest> newSizes = new ArrayList<>();
                    System.out.println("request : "+childRequests.size());
                    System.out.println("entities : "+childEntities.size());
                    for (int i = 0; i < childRequests.size(); i++) {
                        UpdateProductSizeVariantRequest childRequest = childRequests.get(i);
                        if (childRequest.getId() == null) {
                            newSizes.add(childRequest);
                        } else {
                            for (int j = 0; j < childEntities.size(); j++) {
                                ProductSizeVariantEntity childEntity = childEntities.get(j);
                                if (childEntity.getId().compareTo(childRequest.getId()) == 0) {
                                    ids.add(childEntity.getId());
                                    childEntity = productVariantConverter.toEntity(childRequest, childEntity);
                                    saveOrUpdates.add(childEntity);
                                    break;
                                }
                            }
                        }
                    }
                    List<ProductSizeVariantEntity> removes = new ArrayList<>(childEntities
                            .stream()
                            .filter(r -> ids.stream().allMatch(s -> r.getId().compareTo(s) != 0))
                            .toList());
                    int length = Math.abs(removes.size() - saveOrUpdates.size());
                    if (length != 0) {
                        if (removes.size() > saveOrUpdates.size()) {
                            saveOrUpdates.addAll(Collections.nCopies(length, new ProductSizeVariantEntity()));
                        } else {
                            removes.addAll(Collections.nCopies(length, new ProductSizeVariantEntity()));
                        }
                    }
                    System.out.println("save : " + newSizes.size());
                    System.out.println("update : " + saveOrUpdates.size());
                    System.out.println("remove : " + removes.size());
                    return Flux.zip(Flux.fromIterable(saveOrUpdates), Flux.fromIterable(removes))
                            .flatMap(flux -> {
                                ProductSizeVariantEntity saveOrUpdate = flux.getT1();
                                ProductSizeVariantEntity remove = flux.getT2();
                                System.out.println("update name : " + saveOrUpdate.getName());
                                System.out.println("remove name : " + remove.getName());
                                if (saveOrUpdate.getId() == null) {
                                    return productSizeVariantRepository.delete(remove);
                                } else if (remove.getId() == null) {
                                    return productSizeVariantRepository.saveOrUpdate(saveOrUpdate);
                                }
                                return Mono.when(productSizeVariantRepository.saveOrUpdate(flux.getT1()),
                                        productSizeVariantRepository.delete(flux.getT2()));
                            }).then(Mono.defer(()->{
                                if(!newSizes.isEmpty()) {
                                    return Flux
                                            .fromIterable(newSizes)
                                            .flatMap(variant -> {
                                                ProductSizeVariantEntity productSizeVariantEntity = productVariantConverter.toEntity(variant);
                                                productSizeVariantEntity.setProductColorVariantId(productColorVariantId);
                                                System.out.println("save name : " + productSizeVariantEntity.getName());
                                                return productSizeVariantRepository
                                                        .saveOrUpdate(productSizeVariantEntity)
                                                        .then();
                                            }).then();
                                }
                                return Mono.empty();
                            }));
                }).then();
    }

    private Mono<Void> removeAllByProductColorVariant(ProductColorVariantEntity productColorVariantEntity) {
        return productSizeVariantRepository
                .findAllByProductColorVariantId(productColorVariantEntity.getId())
                .flatMap(productSizeVariantRepository::delete).then();
    }
}
