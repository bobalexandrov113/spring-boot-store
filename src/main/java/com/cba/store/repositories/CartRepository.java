package com.cba.store.repositories;

import com.cba.store.entities.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
public Cart getCartWithItems(UUID id);
}