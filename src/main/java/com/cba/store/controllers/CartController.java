package com.cba.store.controllers;

import com.cba.store.dtos.CartDto;
import com.cba.store.entities.Cart;
import com.cba.store.mappers.CartMapper;
import com.cba.store.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/carts")

public class CartController {

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    public CartController(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @PostMapping
    public ResponseEntity<CartDto> createCart() {
        var cart= new Cart();
        cartRepository.save(cart);
        var cartDto = cartMapper.toDto(cart);
        var uri = UriComponentsBuilder.fromHttpUrl("/carts").build().toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }
}
