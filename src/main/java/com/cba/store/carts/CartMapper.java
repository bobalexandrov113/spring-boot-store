package com.cba.store.carts;

import com.cba.store.entities.Cart;
import com.cba.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "totalPrice",expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target= "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
