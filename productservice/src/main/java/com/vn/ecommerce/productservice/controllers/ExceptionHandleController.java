package com.vn.ecommerce.productservice.controllers;

import com.vn.ecommerce.commonservice.exceptions.BadRequestException;
import com.vn.ecommerce.commonservice.exceptions.BaseException;
import com.vn.ecommerce.commonservice.exceptions.NotFoundException;
import com.vn.ecommerce.commonservice.models.ResponseModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@PropertySource("classpath:message.properties")
public class ExceptionHandleController {


    @Value("${sys.internalServerError}")
    private String internalServerError;

    @ExceptionHandler(value = {
            WebExchangeBindException.class,
            BaseException.class,
            MissingRequestValueException.class,
            ServerWebInputException.class
    })
    public Mono<ResponseEntity<?>> handleException(Exception ex) {
        String message = null;
        if (ex instanceof BaseException) {
            message = ((BaseException)ex).getMessage();
        }  else {
            message = ex.getMessage();
        }
        return response(
                HttpStatus.BAD_REQUEST.value(),
                message,
                ResponseEntity.badRequest()
        );
    }






    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResponseEntity<?>> handleInternalServerError(Exception ex) {
        ex.printStackTrace();
        return response(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                internalServerError,
                ResponseEntity.internalServerError()
        );
    }

    public Mono<ResponseEntity<?>>response(Integer status,String errorMessage, ResponseEntity.BodyBuilder builder){
        ResponseModel<Object> res = new ResponseModel<>();
        res.setData(null);
        res.setStatus(status);
        res.setMessage(errorMessage);
        return Mono.just(builder.body(res));
    }

}
