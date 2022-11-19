package com.ciyu.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Current Glossary Not Set")
public class CurrentGlossaryNotSetException extends RuntimeException{
}
