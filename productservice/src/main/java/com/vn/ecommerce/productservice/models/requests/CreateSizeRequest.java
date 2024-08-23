package com.vn.ecommerce.productservice.models.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateSizeRequest {
    @NotBlank(message = "name must be value")
    private String name;
    @NotBlank(message = "type must be value")
    private String type;
}
