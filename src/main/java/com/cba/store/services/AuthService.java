package com.cba.store.services;

import com.cba.store.entities.User;
import com.cba.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor

@Service
public class AuthService {
    UserRepository userRepository;
    public User getCurrentUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long)authentication.getPrincipal();
        return userRepository.findById(userId).orElse(null);
    }
}
