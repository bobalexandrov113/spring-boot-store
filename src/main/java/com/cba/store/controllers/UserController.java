package com.cba.store.controllers;

import com.cba.store.dtos.UserDto;
import com.cba.store.entities.User;
import com.cba.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController

@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository repository;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll()
    {
       List<UserDto> list =repository
               .findAll()
               .stream()
               .map(user->new UserDto(user.getId(), user.getName(), user.getEmail()))
               .toList();

       if(list.isEmpty())
           return ResponseEntity.notFound().build();
       else return ResponseEntity.ok().body(list);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id)
    {
       User user = repository.findById(id).orElse(null);
       if (user == null)
           return ResponseEntity.notFound().build();
       else {
           UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
           return ResponseEntity.ok(userDto);
       }
    }
}
