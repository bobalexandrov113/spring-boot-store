package com.cba.store.controllers;

import com.cba.store.dtos.ErrorDto;
import com.cba.store.dtos.OrderDto;
import com.cba.store.exceptions.OrderNotFoundException;
import com.cba.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllOrders()
    {
       return orderService.getAllOrders();

    }

    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable("orderId") Long id)
    {
        return orderService.getOrder(id);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFoundException()
    {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex)
    {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body( new ErrorDto(ex.getMessage()));
    }


}
