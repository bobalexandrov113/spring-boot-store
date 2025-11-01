package com.cba.store.mappers;

import com.cba.store.dtos.CartDto;
import com.cba.store.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDto toDto(Cart cart);
}
