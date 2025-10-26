package com.cba.store.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterProductRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Min(1)
    private BigDecimal price;

    private byte categoryId;
}
