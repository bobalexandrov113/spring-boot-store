package com.cba.store.mappers;

import com.cba.store.dtos.CartItemDto;
import com.cba.store.entities.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemDto toDto(CartItem cartItem);
}
