package com.cba.store.services;

import com.cba.store.dtos.CheckoutRequest;
import com.cba.store.dtos.CheckoutResponse;
import com.cba.store.entities.Order;
import com.cba.store.exceptions.CartEmptyException;
import com.cba.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {
    public CartService cartService;
    private AuthService authService;
    private OrderRepository orderRepository;


    public CheckoutResponse checkout(CheckoutRequest request) {

        var cart = cartService.getCart(request.getCartId());

        if( cart.isEmpty()) {
            throw new CartEmptyException();
        }
        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return new CheckoutResponse(order.getId());
    }
}
