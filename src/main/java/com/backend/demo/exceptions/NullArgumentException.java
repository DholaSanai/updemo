package com.backend.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullArgumentException extends RuntimeException{
    public NullArgumentException(String msg){
        super(msg);
    }


}
