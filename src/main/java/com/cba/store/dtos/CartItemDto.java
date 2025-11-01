package com.cba.store.dtos;

import com.cba.store.entities.Cart;
import com.cba.store.entities.Product;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class CartItemDto {

    private Long id;


    private Cart cart;



    private Product product;



    private Integer quantity;
}
