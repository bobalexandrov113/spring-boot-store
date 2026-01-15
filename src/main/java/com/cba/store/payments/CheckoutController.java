package com.cba.store.payments;

import com.cba.store.common.ErrorDto;
import com.cba.store.carts.CartEmptyException;
import com.cba.store.carts.CartNotFoundException;
import com.cba.store.orders.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")

@RequiredArgsConstructor

public class  CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;

    @GetMapping("/paymentSuccess/{orderId}")
    public String checkoutSuccess( @PathVariable("orderId") String orderId)
    {
        String html ="<body>";
                html += "Payment for the order " + orderId + " came through successfully";
                html += "</body>";
        return html;
    }

    @PostMapping
    public CheckoutResponse checkout(
    @Valid @RequestBody CheckoutRequest request) {

            return checkoutService.checkout(request);

    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader Map<String,String> headers,
            @RequestBody String payload
    )
    {
            checkoutService.handleWebhookEvent(new WebhookRequest(headers,payload));
            return ResponseEntity.ok().build();
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
