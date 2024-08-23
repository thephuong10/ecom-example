package com.vn.ecommerce.commonservice.exceptions;

import lombok.Getter;

public class BaseException extends RuntimeException{
    @Getter
    private String message;

    public BaseException(String message){
        this.message = message;
    }
}
