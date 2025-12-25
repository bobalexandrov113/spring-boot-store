package com.cba.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    public OrderItem(Order order, Product product, Integer quantity, BigDecimal totalPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.unitPrice = product.getPrice();
    }
}



/*
 order_id bigint not null,
    product_id bigint not null,
    unit_price decimal(10,2) not null,
    quantity int not null,
    total_price decimal(10,2) not null,
    constraint orders_items_id_fk foreign key (order_id) references orders(id),
    constraint product_items_fk foreign key(product_id) references products(id)
 */