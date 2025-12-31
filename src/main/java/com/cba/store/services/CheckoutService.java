package com.cba.store.services;

import com.cba.store.dtos.CheckoutRequest;
import com.cba.store.dtos.CheckoutResponse;
import com.cba.store.entities.Order;
import com.cba.store.exceptions.CartEmptyException;
import com.cba.store.exceptions.CartNotFoundException;
import com.cba.store.repositories.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartService cartService;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    @Value("${websiteUrl}")
    private String websiteUrl;

    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartService.getCart(request.getCartId());

        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }

        if( cart.isEmpty()) {
            throw new CartEmptyException();
        }
        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);

        //Create a checkout Session


        cartService.clearCart(cart.getId());
        return new CheckoutResponse(order.getId());
    }
}
