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
import com.cba.store.services.CheckoutService;
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
    private CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<?> checkout(
    @Valid @RequestBody CheckoutRequest request)
    {
       var checkoutResponse = checkoutService.checkout(request);
       return ResponseEntity.ok(checkoutResponse);
    }
}
