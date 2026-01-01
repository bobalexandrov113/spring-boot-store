package com.cba.store.payments;

import com.cba.store.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class PaymentResult {
    private Long orderId;
    private OrderStatus paymentStatus;
}
