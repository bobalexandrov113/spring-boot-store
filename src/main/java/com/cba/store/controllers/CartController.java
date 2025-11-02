package com.cba.store.controllers;

import com.cba.store.dtos.AddItemToCartRequest;
import com.cba.store.dtos.CartDto;
import com.cba.store.dtos.CartItemDto;
import com.cba.store.entities.Cart;
import com.cba.store.entities.CartItem;
import com.cba.store.mappers.CartItemMapper;
import com.cba.store.mappers.CartMapper;
import com.cba.store.repositories.CartRepository;
import com.cba.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

        var cartItem = cart.getCartItems().stream()
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
            cart.getCartItems().add(cartItem);
        }
        cartRepository.save(cart);
        var cartItemDto = cartItemMapper.toDto(cartItem);
        return ResponseEntity.ok(cartItemDto);
    }
}
