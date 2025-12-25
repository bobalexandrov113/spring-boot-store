package com.cba.store.controllers;

import com.cba.store.dtos.CartDto;
import com.cba.store.dtos.CheckoutRequest;
import com.cba.store.dtos.CheckoutResponse;
import com.cba.store.dtos.ErrorDto;
import com.cba.store.entities.*;
import com.cba.store.exceptions.CartEmptyException;
import com.cba.store.exceptions.CartNotFoundException;
import com.cba.store.mappers.ProductMapper;
import com.cba.store.repositories.CartRepository;
import com.cba.store.repositories.OrderRepository;
import com.cba.store.services.AuthService;
import com.cba.store.services.CartService;
import com.cba.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/checkout")

@AllArgsConstructor
public class CheckoutController {
    private CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout (
    @Valid @RequestBody CheckoutRequest request)
    {
       return checkoutService.checkout(request);
    }
    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex)
    {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
