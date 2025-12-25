package com.cba.store.exceptions;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(String message)
    {
        super(message);
    }
}
