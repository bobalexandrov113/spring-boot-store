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

        var cartItem = cart.getItems().stream()
                .filter(cartItemDto -> cartItemDto.getProduct().getId().equals(product.getId()) )
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }
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

        var cartItem = cart.getItems().stream()
                .filter(cartItemDto -> cartItemDto.getProduct().getId().equals(productId) )
                .findFirst()
                .orElse(null);
        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error","Cart not found")
            );
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toDto(cartItem));

    }
}
