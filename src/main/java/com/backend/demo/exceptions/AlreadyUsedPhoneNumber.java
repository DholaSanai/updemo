package com.backend.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyUsedPhoneNumber extends RuntimeException {
    public AlreadyUsedPhoneNumber(String message) {
        super(message);
    }

}
