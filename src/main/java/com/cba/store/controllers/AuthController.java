package com.cba.store.controllers;

import com.cba.store.dtos.AuthRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request){
       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
       );
        return ResponseEntity.ok().body("Welcome");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e){
        return ResponseEntity.status(401).body(e.getMessage());
    }

}
