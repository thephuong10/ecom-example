package com.vn.ecommerce.productservice.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BaseEntity {
    @Id
    private UUID id;
}
