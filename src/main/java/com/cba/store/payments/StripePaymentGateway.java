package com.cba.store.payments;

import com.cba.store.entities.Order;
import com.cba.store.entities.OrderItem;
import com.cba.store.entities.OrderStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway
{

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        //Create a checkout Session

        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?order_id=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel")
                    .putMetadata("order_id",order.getId().toString());

            order.getItems().forEach(orderItem -> {
                var lineItem = createLineItem(orderItem);
                builder.addLineItem(lineItem);

            });

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        }
            catch (StripeException e) {

                throw new PaymentException(e.getMessage());
            }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {

        try {
            var signature = request.getHeaders().get("stripe-signature");
            var payload = request.getPayload();
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            return switch (event.getType()) {
                case "payment_intent.succeeded" -> Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.PAID));
                case "payment_intent.payment_failed" ->Optional.of(new PaymentResult(extractOrderId(event), OrderStatus.FAILED));
               default ->Optional.empty();

            };


        } catch (SignatureVerificationException e) {
            throw new PaymentException("Signature verification failed");
        }
    }

    private Long extractOrderId(Event event) {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                () -> new PaymentException("Failed to deserialize event")
        );
        var paymentIntent = (PaymentIntent) stripeObject;

            var orderId = paymentIntent.getMetadata().get("order_id");
            return Long.valueOf(orderId);

    }



    private  SessionCreateParams.LineItem createLineItem(OrderItem orderItem) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(orderItem.getQuantity()))
                .setPriceData(createPriceData(orderItem)
                ).build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem orderItem) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("cad")
                .setUnitAmountDecimal(orderItem.getUnitPrice().movePointRight(2))
                .setProductData(createProductData(orderItem)
                ).build();
    }

    private  SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem orderItem) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(orderItem.getProduct().getName())
                .build();
    }


}
