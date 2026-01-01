package com.cba.store.services;

import com.cba.store.dtos.CheckoutRequest;
import com.cba.store.dtos.CheckoutResponse;
import com.cba.store.entities.Order;
import com.cba.store.entities.OrderStatus;
import com.cba.store.exceptions.CartEmptyException;
import com.cba.store.exceptions.CartNotFoundException;
import com.cba.store.exceptions.PaymentException;
import com.cba.store.repositories.OrderRepository;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartService cartService;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;



    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) throws PaymentException {
        var cart = cartService.getCart(request.getCartId());

        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }

        if( cart.isEmpty()) {
            throw new CartEmptyException();
        }
        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);
        try{
            var checkoutSession = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());
            return new CheckoutResponse(order.getId(), checkoutSession.getCheckoutUrl()) ;
         }
         catch (PaymentException e) {
            orderRepository.delete(order);
            throw e;
         }
    }

    public void handleWebhookEvent(WebhookRequest webhookRequest)
    {
            paymentGateway.parseWebhookRequest(webhookRequest)
            .ifPresent(paymentResult -> {

              var orderId = paymentResult.getOrderId();
              var order = orderRepository.findById(orderId).orElseThrow();
              order.setStatus(paymentResult.getPaymentStatus());
              orderRepository.save(order);
          });

    }
}
