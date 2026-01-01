package com.cba.store.orders;

import com.cba.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
