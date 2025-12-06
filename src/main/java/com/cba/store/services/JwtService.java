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

import javax.crypto.SecretKey;
import java.util.Date;
@AllArgsConstructor
@Service
public class JwtService {
    UserRepository userRepository;
    private final JwtConfig jwtConfig;




    public Jwt generateAccessToken(User user)
    {
                return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    public Jwt generateRefreshToken(User user)
    {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    private Jwt generateToken(User user, long tokenExpiration)
    {
                var claims = Jwts.claims()
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("role", user.getRole())
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration*1000))
                .build();

                SecretKey secretKey = jwtConfig.getSecretKey();

                return new Jwt(claims,secretKey);
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

    public Jwt parse(String token)
    {
       try
       {
            return new Jwt(getClaims(token),jwtConfig.getSecretKey());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

//    public String getUsernameFromToken(String token)
//    {
//        return (String) getClaims(token).get("name");
//    }
//    public String getEmailFromToken(String token)
//    {
//        var claims = getClaims(token);
//        return (String) claims.get("email");
//    }
//
//    public Long getUserIdFromToken(String token)
//    {
//        return Long.valueOf(getClaims(token).getSubject());
//    }
//
//    public Role getRoleFromToken(String token)
//    {
//        return Role.valueOf(getClaims(token).get("role").toString());
//    }
}
