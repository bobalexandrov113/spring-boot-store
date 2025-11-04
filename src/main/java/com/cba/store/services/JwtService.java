package com.cba.store.services;

import com.cba.store.dtos.AuthRequestDto;
import com.cba.store.entities.User;
import com.cba.store.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    UserRepository userRepository;

    @Value("${spring.jwt.secret}")
    private String secret;

    public String generateToken(User user)
    {
        final long tokenExpiration = 8640000;


        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email",user.getEmail())
                .claim("name",user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
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
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
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
}
