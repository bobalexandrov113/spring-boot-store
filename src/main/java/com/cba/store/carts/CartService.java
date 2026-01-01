package com.cba.store.carts;

import com.cba.store.entities.Cart;
import com.cba.store.entities.CartItem;
import com.cba.store.products.ProductNotFoundException;
import com.cba.store.products.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    public Cart createCart(  )
    {
        var cart= new Cart();
        cartRepository.save(cart);
        return cart;
    }
    public CartItem addToCart(  UUID cartId, Long productId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }
        var product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        var cartItem=cart.addItem(product);
        cartRepository.save(cart);
        return cartItem;
    }

    public Cart getCart( UUID cartId )
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }
        return cart;
    }

    public CartItem updateCartItem(UUID cartId, Long productId, Integer quantity )
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }

        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new ProductNotFoundException();

        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartItem;

    }

    public void deleteCartItem( UUID cartId, Long productId )
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart( UUID cartId )
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }
        cart.clear();
        cartRepository.save(cart);
    }

}
