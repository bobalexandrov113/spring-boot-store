package com.cba.store.controllers;

import com.cba.store.entities.User;
import com.cba.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController

public class ApiController {
    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    public List<User> findAll()
    {
       List<User> list = new ArrayList<>();
       for (User user : repository.findAll())
       {
           list.add(user);
       }

       return list;

    }
}
