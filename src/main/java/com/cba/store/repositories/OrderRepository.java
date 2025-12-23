package com.cba.store.repositories;

import com.cba.store.entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long>
{

}
