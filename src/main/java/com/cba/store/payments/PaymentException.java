package com.cba.store.payments;

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
