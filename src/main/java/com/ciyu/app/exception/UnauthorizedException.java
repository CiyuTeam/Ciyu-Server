package com.ciyu.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Verification Failed")
public class UnauthorizedException extends RuntimeException{
}
