package com.cba.store.controllers;

import com.cba.store.dtos.CheckoutRequest;
import com.cba.store.dtos.CheckoutResponse;
import com.cba.store.dtos.ErrorDto;
import com.cba.store.exceptions.CartEmptyException;
import com.cba.store.exceptions.CartNotFoundException;
import com.cba.store.exceptions.PaymentException;
import com.cba.store.services.CheckoutService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")

@AllArgsConstructor
public class CheckoutController {
    private CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponse checkout(
    @Valid @RequestBody CheckoutRequest request) {

            return checkoutService.checkout(request);

    }
    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex)
    {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorDto> handlePaymentException(PaymentException e)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(e.getMessage()));
    }

}
