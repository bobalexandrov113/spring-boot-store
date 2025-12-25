package com.cba.store.controllers;

import com.cba.store.dtos.CartDto;
import com.cba.store.dtos.CheckoutRequest;
import com.cba.store.dtos.CheckoutResponse;
import com.cba.store.dtos.ErrorDto;
import com.cba.store.entities.*;
import com.cba.store.mappers.ProductMapper;
import com.cba.store.repositories.CartRepository;
import com.cba.store.repositories.OrderRepository;
import com.cba.store.services.AuthService;
import com.cba.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/checkout")

@AllArgsConstructor
public class CheckoutController {
    private CartService cartService;
    private AuthService authService;
    private ProductMapper productMapper;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<?> checkout(
    @Valid @RequestBody CheckoutRequest request)
    {
      Cart cart ;
      try {
           cart = cartService.getCart(request.getCartId());
      }
      catch (Exception e) {
          //return ResponseEntity.badRequest().body(Map.of("error","Cart not found"));
          return ResponseEntity.badRequest().body(new ErrorDto("Cart not found"));
      }
      if( cart.getTotalPrice().compareTo(BigDecimal.ZERO)==0)
       {
           return ResponseEntity.badRequest().body(
                   new ErrorDto("Cart is empty")
           );
       }

        var order = Order.fromCart(cart, authService.getCurrentUser());
       orderRepository.save(order);
       cartService.clearCart(cart.getId());
       return ResponseEntity.ok(new CheckoutResponse(order.getId()));
    }
}
