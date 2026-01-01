package com.cba.store.orders;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;
@Data
public class OrderProductDto {
    Long id;
    String name;
    @NotNull(message = "Price cannot be null")
    @Min(message = "Price cannot be negative", value = 0)
    BigDecimal price;
}
