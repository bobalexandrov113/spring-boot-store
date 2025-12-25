package com.cba.store.controllers;

import com.cba.store.dtos.*;
import com.cba.store.exceptions.CartItemNotFoundException;
import com.cba.store.exceptions.CartNotFoundException;
import com.cba.store.exceptions.ProductNotFoundException;
import com.cba.store.mappers.CartItemMapper;
import com.cba.store.mappers.CartMapper;
import com.cba.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name="Carts")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;


    @PostMapping
    public ResponseEntity<CartDto> createCart() {
        CartDto cartDto = cartMapper.toDto(cartService.createCart());
        var uri = UriComponentsBuilder.fromHttpUrl("/carts").build().toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }



    @PostMapping("/{cartId}/items")
    @Operation(summary="Adds a product to the cart")
    public ResponseEntity<CartItemDto> addToCart
            (@Parameter(description = "The id of the cart")
                    @PathVariable UUID cartId,
             @Valid @RequestBody AddItemToCartRequest request)
    {
        var productId = request.getProductId();
        var cartItemDto = cartItemMapper.toDto(cartService.addToCart(cartId, productId));
        var uri = UriComponentsBuilder.fromHttpUrl("/carts/" + cartId).build().toUri();
        return ResponseEntity.created(uri).body  (cartItemDto);
    }


    @GetMapping("/{cartId}")
    @Operation(summary="Gets the cart")
    public ResponseEntity<CartDto> getCart(
            @Parameter(description = "The id of the cart")
            @PathVariable UUID cartId) {
        var cartDto = cartMapper.toDto(cartService.getCart(cartId));
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    @Operation(summary="updates the quantity of products in a cart item ")
    public ResponseEntity<?>updateItem
            (
                   @Parameter(description = "The id of the cart")
                   @PathVariable("cartId") UUID cartId,
                   @Parameter(description = "The Product id")
                   @PathVariable("productId") Long productId,
                  @Valid @RequestBody UpdateCartItemRequest request
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
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<?> handleCartNotFound()
        {
         //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Cart not found"));
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Cart not found"));
        }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<?> handleCartItemNotFound()
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Cart Item not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFound()
    {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Product not found"));
    }
}
