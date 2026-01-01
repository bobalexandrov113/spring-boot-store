package com.cba.store.orders;

import com.cba.store.entities.Order;
import com.cba.store.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long>
{

   @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o from Order o WHERE o.customer=:customer")
    List<Order> getOrdersByCustomer(@Param("customer") User customer);

    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o FROM Order o where o.id = :orderId")
    Optional<Order> getOrderWithItems(@Param("orderId") Long id);
}
