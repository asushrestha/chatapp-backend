package com.leapfrog.chattest.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler  extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RestException.class)
    public final ResponseEntity<ErrorResponse> restExceptionHandler(RestException restException){

        ErrorResponse errorResponse=new ErrorResponse(restException.getMessage(),restException.getCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

