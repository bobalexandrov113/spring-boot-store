package com.cba.store.auth;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String email;
    private String password;
    private String username;
    private Long id;
}
