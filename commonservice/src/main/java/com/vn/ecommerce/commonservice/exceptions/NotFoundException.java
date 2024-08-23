package com.vn.ecommerce.commonservice.exceptions;

public class NotFoundException extends BaseException {

    private static final String message = "%s could be not found";

    public NotFoundException(String entity){
        super(String.format(message,entity));
    }

    public NotFoundException(){
        super(String.format(message,"This entity"));
    }

}
