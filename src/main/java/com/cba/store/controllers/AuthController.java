package com.cba.store.controllers;

import com.cba.store.dtos.AuthRequestDto;
import com.cba.store.dtos.JwtResponse;
import com.cba.store.entities.User;
import com.cba.store.mappers.UserMapper;
import com.cba.store.repositories.UserRepository;
import com.cba.store.services.JwtService;
import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthRequestDto request){
       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
       );
       var token = jwtService.generateToken(request.getEmail());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader)
    {
        System.out.println("Validate called");
        var token = authHeader.replace("Bearer ", "");

        return jwtService.validateToken(token);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser()
    {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var email = (String)authentication.getPrincipal();

        var userOptional = userRepository.findByEmail(email);
        if (userOptional == null)
            return ResponseEntity.notFound().build();

        var user = userOptional.get();
        var userDto = userMapper.userToUserDto((User)user);

        return ResponseEntity.ok(userDto);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e){
        return ResponseEntity.status(401).body(e.getMessage());
    }

}
