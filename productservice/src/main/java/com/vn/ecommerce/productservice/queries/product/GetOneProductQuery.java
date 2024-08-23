package com.vn.ecommerce.productservice.queries.product;

import com.vn.ecommerce.productservice.converters.CategoryConverter;
import com.vn.ecommerce.productservice.converters.ProductConverter;
import com.vn.ecommerce.productservice.converters.ProductVariantConverter;
import com.vn.ecommerce.productservice.models.dtos.CategoryDto;
import com.vn.ecommerce.productservice.models.dtos.ProductColorVariantDto;
import com.vn.ecommerce.productservice.models.dtos.ProductDto;
import com.vn.ecommerce.productservice.models.dtos.ProductSizeVariantDto;
import com.vn.ecommerce.productservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class GetOneProductQuery {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final CategoryRepository categoryRepository;
    private final ProductColorVariantRepository productColorVariantRepository;
    private final ProductSizeVariantRepository productSizeVariantRepository;
    private final CategoryConverter categoryConverter;

    private final ProductConverter productConverter;
    private final ProductVariantConverter productVariantConverter;

    @Autowired
    public GetOneProductQuery(ProductRepository productRepository,
                              PromotionRepository promotionRepository,
                              CategoryRepository categoryRepository,
                              ProductColorVariantRepository productColorVariantRepository,
                              ProductSizeVariantRepository productSizeVariantRepository,
                              CategoryConverter categoryConverter,
                              ProductConverter productConverter,
                              ProductVariantConverter productVariantConverter) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.categoryRepository = categoryRepository;
        this.productColorVariantRepository = productColorVariantRepository;
        this.productSizeVariantRepository = productSizeVariantRepository;
        this.categoryConverter = categoryConverter;
        this.productConverter = productConverter;
        this.productVariantConverter = productVariantConverter;
    }

    public Mono<ProductDto>handle(UUID id){
        return productRepository
                .findById(id)
                .flatMap(productEntity->{
                    ProductDto productDto = productConverter.toDto(productEntity);
                    Mono<List<ProductColorVariantDto>>productColorVariantDtoFlux = getProductColorVariants(productDto).share();
                    Mono<CategoryDto>categoryDtoMono = getCategory(productEntity.getCategoryId()).share();
                    return Mono.zip(categoryDtoMono,productColorVariantDtoFlux)
                            .flatMap(tuple->{
                                productDto.setCategory(tuple.getT1());
                                productDto.setChilds(tuple.getT2());
                                return Mono.just(productDto);
                            });
                });
    }



    private Mono<List<ProductColorVariantDto>>getProductColorVariants(ProductDto productDto){
        return productColorVariantRepository
                .findAllByProductId(productDto.getId())
                .flatMap(productColorVariantEntity -> {
                    ProductColorVariantDto productColorVariantDto = productVariantConverter.toDto(productColorVariantEntity);
                    String fullName = StringUtils.hasText(productColorVariantEntity.getName()) ? productDto.getName().concat(" - ") + productColorVariantEntity.getName() : productDto.getName();
                    productColorVariantDto.setFullName(fullName);
                    return getProductSizeVariants(productColorVariantDto)
                            .map(sizes->{
                                productColorVariantDto.setVariants(sizes);
                                return productColorVariantDto;
                            });
                }).collectList();
    }

    private Mono<List<ProductSizeVariantDto>>getProductSizeVariants(ProductColorVariantDto productColorVariantDto){
        return productSizeVariantRepository
                .findAllByProductColorVariantId(productColorVariantDto.getId())
                .flatMap(productSizeVariantEntity -> {
                    ProductSizeVariantDto productSizeVariantDto = productVariantConverter.toDto(productSizeVariantEntity);
                    productSizeVariantDto.setFullName(productColorVariantDto.getFullName().concat(", ") + productSizeVariantEntity.getName());
                    return Mono.just(productSizeVariantDto);
                }).collectList();
    }

    private Mono<CategoryDto>getCategory(UUID categoryId){
        return categoryRepository
                .findById(categoryId)
                .map(categoryConverter::toDto);
    }
}
