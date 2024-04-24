package com.backend.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.WeakKeyException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTauth {

    @Value("${token.expiration_time}")
    private String tokenExpiry;

    @Value("${token.secret}")
    private String tokenSecret;

    public String generateToken(String id) throws InvalidKeyException, WeakKeyException {
        return Jwts.builder()
                .setSubject(id)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(tokenExpiry)))
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
         
    }

    public String validateToken(String token){
        if(StringUtils.isEmpty(token)){
            return StringUtils.EMPTY;
        }
        return Jwts.parser().
                setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8)).
                parseClaimsJws(token).
                getBody().getSubject();
    }


}
