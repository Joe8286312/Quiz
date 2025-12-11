package com.joe.quiz.utils;

import com.joe.quiz.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("mySuperSecretKey1234567890abcdef".getBytes());
    private static final long EXPIRATION_TIME_MS = 60 * 60 * 1000;

    // 修改：提供构建 Claims 的方法，包含角色
    public static Claims buildClaims(User user) {
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("username", user.getUserName());
        claims.put("role", user.getUserRole());
        return claims;
    }

    public static String generateToken(String username) {
        long expirationMillis = 1000 * 60 * 60;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String generateTokenWithClaims(Claims claim) {
        return Jwts.builder()
                .setClaims(claim)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parseTokenReturnClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}