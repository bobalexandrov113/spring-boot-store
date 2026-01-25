package com.cba.store.payments;

import com.cba.store.common.ErrorDto;
import com.cba.store.carts.CartEmptyException;
import com.cba.store.carts.CartNotFoundException;
import com.cba.store.orders.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")

@RequiredArgsConstructor

public class  CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;
    @Value("${baseUrl}")
    private String baseUrl;

    @GetMapping("/paymentSuccess/{orderId}")
    public String checkoutSuccess(@PathVariable("orderId") String orderId, Model model)
    {

       String html="<html><head><title> payment success</title>";
       html += "<style>.payment_notify {\n" +
               "    justify-content: flex-start;\n" +
               "    padding: 0 15px;\n" +
               "    border-radius: 25px;\n" +
               "    border: 2px solid green;\n" +
               "    width: 840px;\n" +
               "    height: 80px;\n" +
               "    box-shadow: 1px 2px 3px 4px rgba(191,191,191,0.6);\n" +
               "    margin-left: auto;\n" +
               "    margin-right: auto;\n" +
               "    text-align: center;\n" +
               "    background: #2A7B9B;\n" +
               "    background: radial-gradient(circle, rgba(42, 123, 155, 1) 0%, rgba(87, 199, 133, 1) 84%);\n" +
               "}</style></head>";
       html += "<body>";
       html +="<div class=payment_notify>";
       html += "<h1 style=\"color: white;\">Payment for order " + orderId +" has been successful</h1>";

       html +="</div></body></html>";

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
