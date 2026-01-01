package com.cba.store.carts;

import com.cba.store.entities.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemDto toDto(CartItem cartItem);
}
