package com.cba.store.payments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class CheckoutResponse {
    private Long orderId;
    private String checkoutUrl;
}
