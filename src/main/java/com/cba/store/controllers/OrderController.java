package com.cba.store.controllers;

import com.cba.store.dtos.OrderDto;
import com.cba.store.mappers.OrderMapper;
import com.cba.store.repositories.OrderRepository;
import com.cba.store.services.AuthService;
import com.cba.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
