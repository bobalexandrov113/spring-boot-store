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
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
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