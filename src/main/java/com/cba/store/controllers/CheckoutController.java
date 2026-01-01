package com.cba.store.controllers;

import com.cba.store.dtos.CheckoutRequest;
import com.cba.store.dtos.CheckoutResponse;
import com.cba.store.dtos.ErrorDto;
import com.cba.store.entities.OrderStatus;
import com.cba.store.exceptions.CartEmptyException;
import com.cba.store.exceptions.CartNotFoundException;
import com.cba.store.exceptions.PaymentException;
import com.cba.store.repositories.OrderRepository;
import com.cba.store.services.CheckoutService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")

@RequiredArgsConstructor

public class CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @PostMapping
    public CheckoutResponse checkout(
    @Valid @RequestBody CheckoutRequest request) {

            return checkoutService.checkout(request);

    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload
    )
    {
        try {
            var event = Webhook.constructEvent(payload,signature,webhookSecretKey);
            System.out.println(event.getType());
            var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
            //charge -> (Charge)stripeObject
            //payment_intent

            switch(event.getType()) {
                case "payment_intent.succeeded" ->{
                    //update order status (PAID)
                    var paymentIntent = (PaymentIntent) stripeObject;
                    if(paymentIntent != null) {
                        var orderId = paymentIntent.getMetadata().get("order_id");
                        var order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow();
                        order.setStatus(OrderStatus.PAID);
                        orderRepository.save(order);
                    }

                }
                case "payment_intent.failed" ->{
                    //update order status (FAILED)
                }
            }
            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

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
