package com.cba.store.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
    private UUID id;
    private List<CartItemDto> cartItemDtos=new  ArrayList<>();
    private BigDecimal price= BigDecimal.ZERO;

}
