package com.cba.store.mappers;

import com.cba.store.dtos.OrderDto;
import com.cba.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
