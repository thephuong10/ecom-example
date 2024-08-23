package com.vn.ecommerce.commonservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResponseModel<T> {

    private Integer status;
    private T data;
    private String message;

}
