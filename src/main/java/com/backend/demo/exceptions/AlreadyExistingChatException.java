package com.backend.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistingChatException extends RuntimeException{
    public AlreadyExistingChatException(String message){
        super(message);
    }
}
