package com.cba.store.carts;

public class CartNotFoundException extends RuntimeException{
    public  CartNotFoundException(String message)
    {
        super(message);
    }
}
