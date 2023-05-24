package com.leapfrog.chattest.exception;

import com.leapfrog.chattest.constants.ErrorMessage;
import lombok.Getter;

@Getter
public class RestException extends RuntimeException{

    private String message;
    private String code;

    public RestException(String message,String code) {
        super(message);
        this.message = message;
        this.code = code;
    }
    public RestException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.message = errorMessage.getMessage();
        this.code = errorMessage.getCode();
    }
}
