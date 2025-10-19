package com.cba.store.controllers;

import com.cba.store.dtos.UserDto;
import com.cba.store.entities.User;
import com.cba.store.mappers.UserMapper;
import com.cba.store.mappers.UserMapperImpl;
import com.cba.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController

@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(required = false, defaultValue = "", name = "sort") String sort
    )
    {
       if(!Set.of("id","name","email").contains(sort))
           sort = "name";
        List<UserDto> list =repository
               .findAll( Sort.by(sort).ascending( ) )
               .stream()
               //.map(user->userMapper.userToUserDto(user))
               .map(userMapper::userToUserDto)
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

           return ResponseEntity.ok(userMapper.userToUserDto(user));
       }
    }
}
