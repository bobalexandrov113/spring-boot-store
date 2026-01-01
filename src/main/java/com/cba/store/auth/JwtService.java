package com.cba.store.auth;

import com.cba.store.entities.User;
import com.cba.store.users.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
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
}
