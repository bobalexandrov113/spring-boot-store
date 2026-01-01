package com.cba.store.carts;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(String message)
    {
        super(message);
    }
}
