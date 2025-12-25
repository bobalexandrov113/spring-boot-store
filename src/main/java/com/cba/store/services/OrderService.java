package com.cba.store.services;

import com.cba.store.dtos.OrderDto;
import com.cba.store.exceptions.OrderNotFoundException;
import com.cba.store.mappers.OrderMapper;
import com.cba.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AuthService authService;

    public List<OrderDto> getAllOrders()
    {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);
        return orders
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public OrderDto getOrder(Long orderId)
    {
        var order =  orderRepository.getOrderWithItems(orderId)
                .orElseThrow(OrderNotFoundException::new);

        var user = authService.getCurrentUser();
        if(!order.isPlacedBy(user))
        {
           throw new AccessDeniedException("You don't have permission to access this order.");
        }
        return orderMapper.toDto(order);
    }
}
