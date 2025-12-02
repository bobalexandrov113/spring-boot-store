package com.cba.store.services;

import com.cba.store.config.JwtConfig;
import com.cba.store.dtos.AuthRequestDto;
import com.cba.store.entities.Role;
import com.cba.store.entities.User;
import com.cba.store.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
@AllArgsConstructor
@Service
public class JwtService {
    UserRepository userRepository;
    private final JwtConfig jwtConfig;




    public String generateAccessToken(User user)
    {

        return getString(user, jwtConfig.getAccessTokenExpiration());
    }

    public String generateRefreshToken(User user)
    {

        return getString(user, jwtConfig.getRefreshTokenExpiration());
    }

    private String getString(User user, long tokenExpiration) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration*1000))
                .signWith(jwtConfig.getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token)
    {
        try {
            var claims = getClaims(token);
              return claims.getExpiration().after(new Date());
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token)
    {
        return (String) getClaims(token).get("name");
    }
    public String getEmailFromToken(String token)
    {
        var claims = getClaims(token);
        return (String) claims.get("email");
    }

    public Long getUserIdFromToken(String token)
    {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public Role getRoleFromToken(String token)
    {
        return Role.valueOf(getClaims(token).get("role").toString());
    }
}
