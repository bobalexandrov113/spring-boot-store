package com.cba.store.services;

import com.cba.store.config.JwtConfig;
import com.cba.store.entities.Role;
import com.cba.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@AllArgsConstructor

public class Jwt {
    private Claims claims;
    private SecretKey key ;


    public boolean isExpired()
    {
        try {

            return claims.getExpiration().before(new Date());
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public Long getUserId()
    {
        return Long.valueOf(claims.getSubject());
    }

    public Role getRole()
    {
        return Role.valueOf(claims.get("role").toString());
    }

    public String toString()
    {
       return Jwts.builder().claims(claims).signWith(SignatureAlgorithm.HS256, key).compact();
    }

}
