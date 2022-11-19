package com.ciyu.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Verification Failed")
public class VerificationFailedException extends RuntimeException{
    public VerificationFailedException(String message) {
        super(message);
    }

    public VerificationFailedException() {
    }
}
