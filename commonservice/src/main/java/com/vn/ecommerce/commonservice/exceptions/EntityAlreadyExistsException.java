package com.vn.ecommerce.commonservice.exceptions;

public class EntityAlreadyExistsException extends BaseException{
    private static final String message = "%s already exists";
    public EntityAlreadyExistsException(String entity) {
        super(String.format(message,entity));
    }
}
