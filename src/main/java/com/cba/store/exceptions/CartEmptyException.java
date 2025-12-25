package com.cba.store.exceptions;

public class CartEmptyException extends RuntimeException{

    public CartEmptyException(){
        super("Cart is empty");
    }
}
