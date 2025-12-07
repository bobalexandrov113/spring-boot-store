package com.cba.store.services;

import com.cba.store.dtos.CartDto;
import com.cba.store.dtos.CartItemDto;
import com.cba.store.entities.Cart;
import com.cba.store.exceptions.CartItemNotFoundException;
import com.cba.store.exceptions.CartNotFoundException;
import com.cba.store.exceptions.ProductNotFoundException;
import com.cba.store.mappers.CartItemMapper;
import com.cba.store.mappers.CartMapper;
import com.cba.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.cba.store.repositories.CartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    public CartDto createCart(  )
    {
        var cart= new Cart();
        cartRepository.save(cart);
        var cartDto = cartMapper.toDto(cart);
        return cartDto;
    }
    public CartItemDto addToCart(  UUID cartId, Long productId)
    {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        var cartItem=cart.addItem(product);

        cartRepository.save(cart);
        var cartItemDto = cartItemMapper.toDto(cartItem);
        return cartItemDto;
    }

    public CartDto getCart( UUID cartId )
    {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItem( UUID cartId, Long productId,Integer quantity )
    {
        var cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new ProductNotFoundException();

        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartItemMapper.toDto(cartItem);

    }

    public void deleteCartItem( UUID cartId, Long productId )
    {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void deleteCart( UUID cartId )
    {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.clear();
        cartRepository.save(cart);
    }

    public List<CartDto> getAllCarts()
    {
         var carts =  cartRepository.findAll();
         List<CartDto> cartList = new ArrayList<>();
         for (var cart : carts) {
            cartList.add(cartMapper.toDto(cart));
         }
         return cartList;
    }
}
