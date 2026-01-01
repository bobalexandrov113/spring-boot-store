package com.cba.store.services;

import com.cba.store.dtos.CheckoutRequest;
import com.cba.store.dtos.CheckoutResponse;
import com.cba.store.entities.Order;
import com.cba.store.exceptions.CartEmptyException;
import com.cba.store.exceptions.CartNotFoundException;
import com.cba.store.repositories.OrderRepository;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartService cartService;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Transactional
    public ResponseEntity<?> checkout(CheckoutRequest request) throws StripeException {
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

        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?order_id=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel?order_id=" + order.getId());

            order.getItems().forEach( orderItem -> {
                var lineItem = SessionCreateParams.LineItem.builder()
                        .setQuantity( Long.valueOf(orderItem.getQuantity()) )
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("cad")
                                .setUnitAmountDecimal( orderItem.getUnitPrice().movePointRight(2))
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(orderItem.getProduct().getName())
                                                .build()
                                ).build()
                        ).build();

                var session = builder.addLineItem(lineItem);

            });

            var session = Session.create(builder.build());
            cartService.clearCart(cart.getId());
            return  ResponseEntity.ok().body(new CheckoutResponse(order.getId(),session.getUrl())) ;
        }
        catch (StripeException e) {
            orderRepository.delete(order);
            throw e;
        }
    }
}
