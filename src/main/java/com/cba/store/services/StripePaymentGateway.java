package com.cba.store.services;

import com.cba.store.dtos.CheckoutResponse;
import com.cba.store.entities.Order;
import com.cba.store.exceptions.PaymentException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentGateway implements PaymentGateway
{

    @Value("${websiteUrl}")
    private String websiteUrl;


    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        //Create a checkout Session

        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?order_id=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel?order_id=" + order.getId());

            order.getItems().forEach(orderItem -> {
                var lineItem = SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(orderItem.getQuantity()))
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("cad")
                                .setUnitAmountDecimal(orderItem.getUnitPrice().movePointRight(2))
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(orderItem.getProduct().getName())
                                        .build()
                                ).build()
                        ).build();

                var session = builder.addLineItem(lineItem);

            });

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        }
            catch (StripeException e) {

                throw new PaymentException(e.getMessage());
            }
    }
}
