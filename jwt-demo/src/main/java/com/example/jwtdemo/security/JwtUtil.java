package com.example.jwtdemo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpirationTime;

    public String getUsernameFromToken(String token) {
        return parseToken(token).getBody().getSubject();
    }

    public String getUserRoleFromToken(String token) {
        return parseToken(token).getBody().get("userRole", String.class);
    }

    public String generateToken(String username) {
        long nowMillis = System.currentTimeMillis();
        long expirationMillis = nowMillis + jwtExpirationTime;

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(expirationMillis))
                .signWith(SignatureAlgorithm.HS256, getSignKey())
                .compact();
    }

    public boolean validateToken(String token, CustomUserDetails customUserDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(customUserDetails.getUsername());
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    private byte[] getSignKey(){
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }
}
