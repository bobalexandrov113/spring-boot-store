package com.cba.store.dtos;


import com.cba.store.entities.Cart;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddItemToCartRequest {
private Long productId;

}
