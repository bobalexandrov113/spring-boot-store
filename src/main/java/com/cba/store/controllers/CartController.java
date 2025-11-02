package com.cba.store.controllers;

import com.cba.store.dtos.AddItemToCartRequest;
import com.cba.store.dtos.CartDto;
import com.cba.store.dtos.CartItemDto;
import com.cba.store.dtos.UpdateCartItemRequest;
import com.cba.store.entities.Cart;
import com.cba.store.entities.CartItem;
import com.cba.store.mappers.CartItemMapper;
import com.cba.store.mappers.CartMapper;
import com.cba.store.repositories.CartRepository;
import com.cba.store.repositories.ProductRepository;
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

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;


    @PostMapping
    public ResponseEntity<CartDto> createCart() {
        var cart= new Cart();
        cartRepository.save(cart);
        var cartDto = cartMapper.toDto(cart);
        var uri = UriComponentsBuilder.fromHttpUrl("/carts").build().toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart
            (@PathVariable UUID cartId,
             @RequestBody AddItemToCartRequest request)
    {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }
        var cartItem=cart.addItem(product);

        cartRepository.save(cart);
        var cartItemDto = cartItemMapper.toDto(cartItem);
        var uri = UriComponentsBuilder.fromHttpUrl("/carts/" + cart.getId()).build().toUri();
        return ResponseEntity.created(uri).body(cartItemDto);
    }


    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        var cartDto = cartMapper.toDto(cart);
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
        var cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error","Cart not found")
            );
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toDto(cartItem));

    }


    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
    )
    {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found")
            );
        }
            cart.removeItem(productId);
            cartRepository.save(cart);
            return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
   public ResponseEntity<?>  clearCart(
           @PathVariable UUID cartId
   )
    {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
        }
        cart.clear();
        cartRepository.save(cart);
        return ResponseEntity.noContent().build();
    }


}
