package com.cba.store.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.cba.store.entities.Product}
 */
@Value
public class CartProductDto implements Serializable {
    Long id;
    String name;
    @NotNull(message = "Price cannot be null")
    @Min(message = "Price cannot be negative", value = 0)
    BigDecimal price;
}