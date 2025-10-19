package com.cba.store.controllers;

import com.cba.store.dtos.ChangePasswordRequest;
import com.cba.store.dtos.RegisterUserRequest;
import com.cba.store.dtos.UpdateUserRequest;
import com.cba.store.dtos.UserDto;
import com.cba.store.entities.User;
import com.cba.store.mappers.UserMapper;
import com.cba.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
            @RequestHeader(name = "x-auth-token") String authToken,
            @RequestParam(required = false, defaultValue = "", name = "sort") String sort
    )
    {
        System.out.println(authToken);
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
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody RegisterUserRequest request) {
        var user = userMapper.toEntity(request);
        System.out.println(user);
        repository.save(user);
        UserDto userDto = userMapper.userToUserDto(user);
        var uri = URI.create("/users/" + user.getId());
        return ResponseEntity.created(uri).body(userDto);

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

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Long id, @RequestBody UpdateUserRequest request)
    {
        var user = repository.findById(id).orElse(null);
        if(user == null)
            return ResponseEntity.notFound().build();
        else {
            userMapper.updateUser(request, user);
            repository.save(user);
            return ResponseEntity.ok().body(userMapper.userToUserDto(user));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id)
    {
        var user = repository.findById(id).orElse(null);
        if(user == null)
            return ResponseEntity.notFound().build();
        else {
            repository.delete(user);
            return ResponseEntity.noContent().build();
        }
    }


    @PostMapping("/{id}/change-password")
    public  ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request)
    {
        var user = repository.findById(id).orElse(null);
        if(user == null)
            return ResponseEntity.notFound().build();

        if(!user.getPassword().equals(request.getOldPassword()))
        {
            return new ResponseEntity<>("Old password doesn't match", HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(request.getNewPassword());
        repository.save(user);
        return ResponseEntity.noContent().build();

    }
}
