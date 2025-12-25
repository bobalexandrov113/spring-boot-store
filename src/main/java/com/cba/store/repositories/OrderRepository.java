package com.cba.store.repositories;

import com.cba.store.dtos.OrderDto;
import com.cba.store.entities.Order;
import com.cba.store.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long>
{

    List<Order> findAllByCustomer(User customer);
}
