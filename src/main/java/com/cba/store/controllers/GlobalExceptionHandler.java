package com.cba.store.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex)
    {
        var errors = new HashMap<String,String>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach((fieldError) -> {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                });
        return ResponseEntity.badRequest().body(errors);
    }
}
