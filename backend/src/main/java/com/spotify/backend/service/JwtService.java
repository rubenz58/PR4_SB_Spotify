package com.spotify.backend.service;

import com.spotify.backend.entity.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(User user) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", user.getId());
        payload.put("email", user.getEmail());

        return Jwts.builder()
                .claims(payload)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extract user ID from JWT token
     */
    public String extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Object userIdObj = claims.get("user_id");

            if (userIdObj != null) {
                return userIdObj.toString();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Invalid token
        }
    }

    /**
     * Validate JWT token
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();

            // Check if token is expired
            return !expiration.before(new Date());
        } catch (Exception e) {
            return false;  // Invalid token
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extract all claims from token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}