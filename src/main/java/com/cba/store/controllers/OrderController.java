package com.cba.store.controllers;

import com.cba.store.dtos.OrderDto;
import com.cba.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    //private final AuthService  authService;
//    private final OrderRepository orderRepository;
//
//    public List<OrderDto> getAllOrders()
//    {
//       // var user = authService.getCurrentUser();
//
//        return orderRepository.findAll();
//    }
//
//
//    }
}
