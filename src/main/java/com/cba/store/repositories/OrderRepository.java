package com.cba.store.repositories;

import com.cba.store.entities.Order;
import com.cba.store.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long>
{

   @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o from Order o WHERE o.customer=:customer")
    List<Order> getAllByCustomer(@Param("customer") User customer);
}
