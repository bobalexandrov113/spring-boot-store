package com.cba.store.controllers;

import com.cba.store.dtos.AuthRequestDto;
import com.cba.store.dtos.UserDto;
import com.cba.store.mappers.UserMapper;
import com.cba.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request){
        var user = userRepository.findByEmail(request.getEmail());
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to see this page");
        }

        return ResponseEntity.ok().body("Welcome");
    }

}
