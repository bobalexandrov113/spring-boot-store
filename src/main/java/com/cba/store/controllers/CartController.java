package com.cba.store.controllers;

import com.cba.store.dtos.AddItemToCartRequest;
import com.cba.store.dtos.CartDto;
import com.cba.store.dtos.CartItemDto;
import com.cba.store.dtos.UpdateCartItemRequest;
import com.cba.store.exceptions.CartItemNotFoundException;
import com.cba.store.exceptions.CartNotFoundException;
import com.cba.store.exceptions.ProductNotFoundException;
import com.cba.store.services.CartService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")

public class CartController {

    private final CartService cartService;


    @PostMapping
    public ResponseEntity<CartDto> createCart() {
        CartDto cartDto = cartService.createCart();
        var uri = UriComponentsBuilder.fromHttpUrl("/carts").build().toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart
            (@Parameter(description = "The id of the cart")
                    @PathVariable UUID cartId,
             @RequestBody AddItemToCartRequest request)
    {
        var productId = request.getProductId();
        var cartItemDto = cartService.addToCart(cartId,productId);
        var uri = UriComponentsBuilder.fromHttpUrl("/carts/" + cartId).build().toUri();
        return ResponseEntity.created(uri).body(cartItemDto);
    }


    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        var cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?>updateItem
            (
                   @PathVariable("cartId") UUID cartId,
                   @PathVariable("productId") Long productId,
                   @RequestBody UpdateCartItemRequest request
            )
    {
        var quantity = request.getQuantity();
        var cartItem = cartService.updateCartItem(cartId,productId,quantity);
        return ResponseEntity.ok(cartItem);

    }


    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
    )
    {
       cartService.deleteCartItem(cartId,productId);
       return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
   public ResponseEntity<?>  clearCart(
           @PathVariable UUID cartId
   )
    {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound()
        {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Cart not found"));
        }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartItemNotFound()
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Cart item not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound()
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found"));
    }
}
