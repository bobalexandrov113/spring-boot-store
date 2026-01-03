package com.cba.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cart_items",  uniqueConstraints = {
        @UniqueConstraint(name = "cart_items_unique", columnNames = {"product_id", "cart_id"})
})
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "cart_id")
    private Cart cart;


    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;


    @Column(name = "quantity")
    private Integer quantity;

    public BigDecimal getTotalPrice()
    {
        var price = product.getPrice();
        var quantity = this.getQuantity();
        return price.multiply(BigDecimal.valueOf(quantity));

    }

}