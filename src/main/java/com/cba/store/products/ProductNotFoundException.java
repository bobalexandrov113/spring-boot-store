package com.cba.store.products;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(){
        super("Product not found");
    }
}
