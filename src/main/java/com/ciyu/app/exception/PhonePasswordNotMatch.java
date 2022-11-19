package com.ciyu.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Phone and Password Not Match")
public class PhonePasswordNotMatch extends RuntimeException{
}
