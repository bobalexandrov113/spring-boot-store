package com.cba.store.payments;

import com.cba.store.entities.Order;
import com.cba.store.carts.CartEmptyException;
import com.cba.store.carts.CartNotFoundException;
import com.cba.store.entities.OrderStatus;
import com.cba.store.orders.OrderRepository;

import com.cba.store.auth.AuthService;
import com.cba.store.carts.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartService cartService;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;



    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) throws PaymentException {
        var cart = cartService.getCart(request.getCartId());

        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }

        if( cart.isEmpty()) {
            throw new CartEmptyException();
        }
        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);
        try{
            var checkoutSession = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());
            return new CheckoutResponse(order.getId(), checkoutSession.getCheckoutUrl()) ;
         }
         catch (PaymentException e) {
            orderRepository.delete(order);
            throw e;
         }
    }

    public void handleWebhookEvent(WebhookRequest webhookRequest)
    {
             var result = paymentGateway.parseWebhookRequest(webhookRequest);
            result.ifPresent(paymentResult -> {
             System.out.println(" ****************** " + paymentResult.toString() + "++++++++++++");
              var orderId = paymentResult.getOrderId();
              var order = orderRepository.findById(orderId).orElseThrow();
              order.setStatus(OrderStatus.PAID);
              orderRepository.save(order);
          });
            if (result.isEmpty())
            {
                System.out.println("************* Got no result from webhook *************");
            }


                                ;

    }
}
