package com.cba.store.controllers;

import com.cba.store.dtos.CheckoutRequest;
import com.cba.store.dtos.CheckoutResponse;
import com.cba.store.repositories.CartRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/checkout")

@AllArgsConstructor
public class CheckoutController {
    private CartRepository cartRepository;

    @PostMapping
    public ResponseEntity<?> checkout(
         @Valid @RequestBody CheckoutRequest request)
    {
       var cart = cartRepository.findById(request.getCartId());
       if (cart == null) {
           return ResponseEntity.badRequest().body(
                   Map.of("error","Cart not found")
           );
       }
       return ResponseEntity.ok(cart);


    }
}
