package com.cba.store.services;

import com.cba.store.dtos.CheckoutRequest;
import com.cba.store.dtos.CheckoutResponse;
import com.cba.store.entities.Order;
import com.cba.store.exceptions.CartEmptyException;
import com.cba.store.exceptions.CartNotFoundException;
import com.cba.store.exceptions.PaymentException;
import com.cba.store.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
