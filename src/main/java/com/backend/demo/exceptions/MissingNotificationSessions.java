package com.backend.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MissingNotificationSessions extends RuntimeException{
    public MissingNotificationSessions(String message){
        super(message);
    }
}
